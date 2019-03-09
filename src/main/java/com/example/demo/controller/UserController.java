package com.example.demo.controller;

import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dao.ProductCategoryDao;
import com.example.demo.model.pojo.Category;
import com.example.demo.model.repository.CategoryRepository;
import com.example.demo.utility.exceptions.ProductExceptions.CategoryAlreadyExistsException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.model.pojo.User;
import com.example.demo.utility.exceptions.UserExceptions.*;
import com.example.demo.utility.mail.MailUtil;
import net.bytebuddy.utility.RandomString;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductCategoryDao productCategoryDao;

    public String getPath(){
        String path = "C:" + File.separator + "Users" + File.separator +
                "Prod" + File.separator + "Desktop" + File.separator + "images" + File.separator;
        return path;
    }

    //Register form
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerUser(@RequestBody User u, HttpServletResponse response) throws Exception {

        if (userValidator.validateEmptyFields(u)) {
                if (userRepository.findByEmail(u.getEmail()) == null) {
                    u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt()));
                    userRepository.save(u);

                    new Thread(() -> {
                        try {
                            MailUtil.sendMail(serverEmailAddress, u.getEmail(), "Account verification", MailUtil.CONFIRM_MESSAGE + u.getUserId());
                        } catch (MessagingException e) {
                            //TODO Deal with email not sending
                        }
                    }).start();
                }
                else {
                    throw new UserAlreadyExistsException();
                }
        }
        response.getWriter().append("You have been registered successfully, please verify your account by entering your email address.");
    }

    //Verifying user after registration
    @RequestMapping(value = "/users/verify/{userId}", method = RequestMethod.GET)
    public void verifyUser(@PathVariable("userId") long userId, HttpServletResponse response) throws Exception{

        if (userRepository.existsById(userId)) {

            User user = userRepository.findByUserId(userId);
            if (user.getVerified() != User.IS_USER_VERIFIED) {
                user.setVerified(1);
                userRepository.saveAndFlush(user);
                response.getWriter().append("You have successfully verified your account! \nRedirecting to the login page in 5 seconds...");
                response.setHeader("Refresh", "5; URL=http://localhost:1337/products");
            }
            else {
                throw new UserAlreadyVerifiedException();
            }
        }
        else {
            throw new UserNotFoundException();
        }
    }

    //Shows all users
    @RequestMapping (value = "/users", method = RequestMethod.GET)
    public List<User> showAllUsers(HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {

        validateAdminLogin(session);
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UsersNotAvailableException();
        }
        return users;
    }

    //Shows user by ID
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
        public User showUserById(@PathVariable("userId") long userId, HttpSession session) throws TechnoMarketException {

        validateAdminLogin(session);
        if (!userRepository.existsById(userId)){
            throw new UserNotFoundException();
        }
        return userRepository.findByUserId(userId);
    }

    //Login user
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User logIn(@RequestBody User u, HttpSession session) throws TechnoMarketException {


        if (userValidator.validateLoginFields(u)) {
            String crypted = userDao.findHashedPassword(u.getEmail());
            if (BCrypt.checkpw(u.getPassword(), crypted)) {
                if (userRepository.findByEmailAndPassword(u.getEmail(), crypted) != null) {
                    User user = userRepository.findByEmailAndPassword(u.getEmail(), crypted);
                    if (user.getVerified() != User.IS_USER_VERIFIED) {
                        throw new UserNotActiveException();
                    }
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("userLogged", user);
                    return user;
                }
                throw new UserNotFoundException();
            }
            throw new UserNotFoundException();
        }
        throw new UserNotFoundException();
    }

    //Logout User
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut (HttpSession session) throws Exception {

        validateLogin(session);
        session.invalidate();

        return "You have successfully logged out.";
    }

    //Forgotten password
    @RequestMapping(value = "/login/forgottenPassword/{email}", method = RequestMethod.GET)
    public void forgottenPassword(@PathVariable("email") String email, HttpServletResponse response) throws Exception {

        //If user exists
        if (userDao.getUserId(email) > 0) {

            String randomPassword = RandomString.make(8);
            MailUtil.sendMail(serverEmailAddress, email, "Reset password", MailUtil.FORGOTTEN_PASSWORD +     randomPassword);
            userValidator.validatePassword(randomPassword);

            User user = userRepository.findByEmail(email);

            if (user != null) {
                user.setPassword(BCrypt.hashpw(randomPassword, BCrypt.gensalt()));
                userRepository.saveAndFlush(user);
            }
            response.getWriter().append("An Email with your new password has been sent, please check your email.");
        }
        else {
            throw new UserNotFoundException();
        }

    }


    @RequestMapping(value = "/users/delete/{userId}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable("userId") long userId, HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {

        validateAdminLogin(session);

            if (userRepository.existsById(userId)) {
                userRepository.delete(userRepository.findByUserId(userId));
                response.getWriter().append("User with ID " + userId + " deleted successfully!");
            }

        throw new UserNotFoundException();
    }

    //Edits user profile
    //Not working
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public String editUser(@RequestBody User u, HttpSession session) throws TechnoMarketException {
        validateLogin(session);

            if (userValidator.validateEmptyFields(u)) {
                userRepository.saveAndFlush(u);
                return "Profile edited successfully!";
            }
            throw new TechnoMarketException("Oops, something went wrong on our side :(");
    }

    //Adds a category to the DB
    @RequestMapping(value = "/products/categories/add", method = RequestMethod.GET)
    public void addCategory(@RequestBody Category category, HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException{
        validateAdminLogin(session);

        if(productCategoryDao.categoryExists(category.getCategoryName())) {
            categoryRepository.save(category);
            response.getWriter().append("Category: " + category.getCategoryName() + " added successfully with ID " + category.getId());
        }
        else {
            throw new CategoryAlreadyExistsException();
        }
    }

}
