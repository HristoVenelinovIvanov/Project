package com.example.demo.controller;

import com.example.demo.model.dao.UserDao;
import com.example.demo.model.dao.ProductCategoryDao;
import com.example.demo.model.pojo.Category;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.repository.CategoryRepository;
import com.example.demo.utility.exceptions.OrderExceptions.NoOrdersAvailableException;
import com.example.demo.utility.exceptions.ProductExceptions.CategoryAlreadyExistsException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.model.pojo.User;
import com.example.demo.utility.exceptions.UserExceptions.*;
import com.example.demo.utility.mail.MailUtil;
import com.github.lambdaexpression.annotation.RequestBodyParam;
import net.bytebuddy.utility.RandomString;
import org.apache.log4j.Priority;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    //Register form
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerUser(@RequestBody User u, HttpServletResponse response) throws Exception {

        if (userValidator.validateEmptyFields(u) && userRepository.findByEmail(u.getEmail()) == null) {
                    u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt()));
                    userRepository.save(u);

                    new Thread(() -> {
                        try {
                            MailUtil.sendMail(serverEmailAddress, u.getEmail(), "Account verification", MailUtil.USERS_VERIFY_MESSAGE + u.getUserId());
                        } catch (MessagingException e) {
                            log.log(Priority.WARN, e.getMessage(), e);
                        }
                    }).start();
                }
                else {
                    throw new UserAlreadyExistsException();
                }
        response.getWriter().append("You have been registered successfully, please verify your account by entering your email address.");
        }
  //  }

    //Verifying user after registration
    @RequestMapping(value = "/users/verify/{userId}", method = RequestMethod.GET)
    public void verifyUser(@PathVariable("userId") long userId, HttpServletResponse response) throws Exception{

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUserId(userId));
        if (userOptional.isPresent()) {
            if (userOptional.get().getVerified() != User.IS_USER_VERIFIED) {
                userOptional.get().setVerified(1);
                userRepository.saveAndFlush(userOptional.get());
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
    public List<User> showAllUsers(HttpSession session) throws TechnoMarketException, IOException {

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
    @RequestMapping(value = "/login/forgottenPassword", method = RequestMethod.POST)
    public void forgottenPassword(@RequestBodyParam String email, HttpServletResponse response) throws Exception {

        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        //If user exists
        if (optionalUser.isPresent()) {

            String randomPassword = RandomString.make(10);
            MailUtil.sendMail(serverEmailAddress, email, "Forgotten password", MailUtil.FORGOTTEN_PASSWORD_FIRST_PART +
                    MailUtil.NEW_FORGOTTEN_PASSWORD +
                    randomPassword +
                    MailUtil.FORGOTTEN_PASSWORD_LINK + optionalUser.get().getUserId() + MailUtil.RESET_PASSWORD_URL +
                    randomPassword +
                    MailUtil.FORGOTTEN_PASSWORD_SECOND_PART);

            response.getWriter().append("An Email with your new password has been sent, please check your email.");
        }
        else {
            throw new UserNotFoundException();
        }
    }

    @RequestMapping(value = "/users/{userId}/resetPassword/{newPassword}", method = RequestMethod.GET)
    public void resetPassword(@PathVariable("userId") long userId, @PathVariable("newPassword") String newPassword, HttpServletResponse response) throws Exception {

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUserId(userId));

        if(userOptional.isPresent()) {
            userOptional.get().setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userRepository.saveAndFlush(userOptional.get());
            response.getWriter().append("Your password has been reset successfully.");
        }
        else {
            throw new UserNotFoundException();
        }
    }


    //Deletes a user only if the id is different than the logged user
    @RequestMapping(value = "/users/{userId}/delete", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("userId") long userId, HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {

        validateAdminLogin(session);

        if(userId == (Long) session.getAttribute("userId")) {
            response.getWriter().append("Why would you want to delete yourself?\nP.S You can't :)");
            return;
        }

        if (userRepository.existsById(userId)) {
            userRepository.delete(userRepository.findByUserId(userId));
            response.getWriter().append("User with ID " + userId + " deleted successfully!");
            return;
        }
        throw new UserNotFoundException();
    }

    //Edits user profile
    @RequestMapping(value = "/users/edit", method = RequestMethod.POST)
    public String editUser(@RequestBodyParam String password, HttpSession session) throws TechnoMarketException {
        validateLogin(session);

        return userValidator.editProfile((User) session.getAttribute("userLogged"), password);
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

    //Views order for the logged user
    @RequestMapping(value = "/profile/orders", method = RequestMethod.GET)
    public List<Order> viewOrders(HttpSession session) throws TechnoMarketException {
        validateLogin(session);

        User user = (User) session.getAttribute("userLogged");

        if (!user.getOrders().isEmpty()) {
            return user.getOrders();

        }
        throw new NoOrdersAvailableException();
    }

    @RequestMapping(value = "/profile/subscribe", method = RequestMethod.GET)
    public void subscribe(HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {
        validateLogin(session);

        User user = (User) session.getAttribute("userLogged");
        user.setSubscribed(1);
        userRepository.saveAndFlush(user);
        response.getWriter().append("You have now subscribed to our website.");
    }

    @RequestMapping(value = "/profile/unSubscribe", method = RequestMethod.GET)
    public void unSubscribe(HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {
        validateLogin(session);

        User user = (User) session.getAttribute("userLogged");
        user.setSubscribed(0);
        userRepository.saveAndFlush(user);
        response.getWriter().append("You have now unsubscribed from our website.");
    }

}
