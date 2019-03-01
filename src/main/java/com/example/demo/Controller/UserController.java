package com.example.demo.Controller;

import com.example.demo.Model.DAO.UserDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UsersNotAvailableException;
import com.example.demo.Model.Utility.MailUtil;
import com.example.demo.Model.Validators.UserValidator;
import com.github.lambdaexpression.annotation.EnableRequestBodyParam;
import com.github.lambdaexpression.annotation.RequestBodyParam;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController
@EnableRequestBodyParam
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    protected UserDao userDao;

    Map<Integer, Enumeration<Product>> productsInCart = new HashMap<>();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    //99% finished
    public void registerUser(@RequestBody User u, HttpServletResponse response) throws Exception {

        if (userValidator.validateEmptyFields(u)) {
            if (userRepository.findByEmail(u.getEmail()) == null) {

                u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt()));
                userRepository.save(u);

                new Thread( () -> {
                    try {
                        MailUtil.sendMail(serverEmailAddress, u.getEmail(), "Account verification", MailUtil.CONFIRM_MESSAGE);
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

    //Shows all users
    @RequestMapping (value = "/users", method = RequestMethod.GET)
    public List<User> showAllUsers() throws UsersNotAvailableException{
            //TODO Validate ADMIN ONLY

            List<User> users = userRepository.findAll();

            if (users.size() == 0) {
                throw new UsersNotAvailableException();
            }
        return users;
    }

    //Shows user by ID
    @RequestMapping (value = "/users/{userId}", method = RequestMethod.GET)
        public User showUserById(@PathVariable("userId") long userId, HttpSession session) throws UsersNotAvailableException{
            //TODO Validate ADMIN ONLY

            User user = userRepository.findByUserId(userId);

            if (user != null) {
                session.setAttribute("userId", user);
                return user;
            }
            throw new UsersNotAvailableException();
    }

    //Login user
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User logIn(@RequestBody User u, HttpSession session) throws TechnoMarketException {
        //90% finished
        if (userValidator.validateLoginFields(u)) {

            String crypted = userDao.findHashedPassword(u.getEmail());

            if (BCrypt.checkpw(u.getPassword(), crypted)) {

                if (userRepository.findByEmailAndPassword(u.getEmail(), crypted) != null) {
                    User user = userRepository.findByEmailAndPassword(u.getEmail(), crypted);
                    session.setAttribute("userLogged", user);
                    return user;
                }
                throw new UserNotFoundExeption();
            }
            throw new UserNotFoundExeption();
        }
        throw new UserNotFoundExeption();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logOut (Exception e, HttpSession session, HttpServletResponse response) throws Exception {
        validateLogin(session, e);
        if (session != null) {
            //A little bit overkill?
            session = null;
            session.invalidate();
            response.sendRedirect("/login");
        }
        else {
            response.sendRedirect("/login");
        }
    }

    @RequestMapping(value = "/login/forgottenPassword/{email}", method = RequestMethod.GET)
    public void forgottenPassword(@PathVariable("email") String email, HttpServletResponse response) throws MessagingException, IOException {

        MailUtil.sendMail(serverEmailAddress, email, "Reset password", MailUtil.FORGOTEN_PASSWORD + userDao.getUserId(email));
        response.getWriter().append("Email has been sent, please check your email.");

    }

    @RequestMapping(value = "/users/resetPassword/{userId}", method = RequestMethod.POST)
    public void resetForgottenPassword(@PathVariable("userId") long userId, @RequestBodyParam(name = "password") String password, HttpServletResponse response) throws Exception {

        User user = userRepository.findByUserId(userId);

        if (user != null) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            userRepository.save(user);
            MailUtil.sendMail(serverEmailAddress, user.getEmail(), "Password reset", MailUtil.PASSWORD_RESET);
            response.getWriter().append("Password reset successfully! \nRedirecting to login page...");
            //TODO Waits 5 seconds then redirect to /login
        }
        else {
            throw new UserNotFoundExeption();
        }
    }


    @RequestMapping(value = "products/addToFavorites", method = RequestMethod.POST)
    public void addToFavorites(@PathVariable("userId") long userId, @PathVariable("productId") long productId) {
        //TODO if logged - add to user favorites
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void deleteUser(HttpSession session, HttpServletResponse response) throws IOException {
        //TODO if Admin - delete user
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void editUser(HttpSession session, HttpServletResponse response) throws IOException {
        //TODO if logged - edit user profile
    }

}
