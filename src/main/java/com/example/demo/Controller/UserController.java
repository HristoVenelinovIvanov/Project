package com.example.demo.Controller;

import com.example.demo.Model.DAO.UserDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.*;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Mail.MailUtil;
import com.example.demo.Model.Utility.Validators.UserValidator;
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
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    protected UserDao userDao;


//    Map<Integer, Enumeration<Product>> productsInCart = new HashMap<>();

    //Register form
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    //99% finished
    public void registerUser(@RequestBody User u, HttpServletResponse response) throws Exception {

        if (userValidator.validateEmptyFields(u)) {
                if (userRepository.findByEmail(u.getEmail()) == null) {
                    u.setPassword(BCrypt.hashpw(u.getPassword(), BCrypt.gensalt()));
                    userRepository.save(u);

                    new Thread(() -> {
                        try {
                            MailUtil.sendMail(serverEmailAddress, u.getEmail(), "Account verification", MailUtil.CONFIRM_MESSAGE + u.getUserId());
                        } catch (MessagingException e) {
                            //TODO Deal with email not sending AND make the method in transaction
                        }
                    }).start();
                } else {
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
            if (user.getVerified() == 0) {
                user.setVerified(1);
                userRepository.save(user);
                response.getWriter().append("You have successfully verified your account! \nRedirecting to the login page in 5 seconds...");
                response.setHeader("Refresh", "5; URL=http://localhost:1337/login");
                //redirect to user; should be made by frontend-er to redirect to proper view
            }
            else {
                throw new UserAlreadyVerifiedException();
            }
        }
        else {
            throw new UserNotFoundExeption();
        }
    }

    //try to start Thread to make redirect after 5 sec
//    public void showMessage(HttpServletResponse response){
//        Thread message = new Thread(() -> {
//            try {
//                response.getWriter().append("You have successfully verified your account! \nRedirecting to the login page in 5 seconds...");
//            } catch (IOException e) {
//                System.out.println("lls" + e.getMessage());
//            }
//        });
//        message.setDaemon(true);
//        message.start();
//    }

    //Shows all users
    @RequestMapping (value = "/users", method = RequestMethod.GET)
    public List<User> showAllUsers() throws UsersNotAvailableException {
        //TODO Validate ADMIN ONLY

        List<User> users = userRepository.findAll();
            //validate user
            if (users.size() == 0) {
                throw new UsersNotAvailableException();
            }
            return users;
        }

    //Shows user by ID
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
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


        if (userValidator.validateLoginFields(u)) {

            String crypted = userDao.findHashedPassword(u.getEmail());

            if (BCrypt.checkpw(u.getPassword(), crypted)) {

                if (userRepository.findByEmailAndPassword(u.getEmail(), crypted) != null) {
                    User user = userRepository.findByEmailAndPassword(u.getEmail(), crypted);
                    if (user.getVerified() == 0) {
                        throw new UserNotActiveException();
                    }
                    session.setAttribute("userId", user.getUserId());
                    session.setAttribute("userLogged", user);
                    return user;
                }
                throw new UserNotFoundExeption();
            }
            throw new UserNotFoundExeption();
        }
        throw new UserNotFoundExeption();
    }

    //Logout User
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

    //Forgotten password
    @RequestMapping(value = "/login/forgottenPassword/{email}", method = RequestMethod.GET)
    public void forgottenPassword(@PathVariable("email") String email, HttpServletResponse response) throws Exception {

        if (userRepository.findByEmail(email) != null) {
            MailUtil.sendMail(serverEmailAddress, email, "Reset password", MailUtil.FORGOTTEN_PASSWORD + userDao.getUserId(email));
            response.getWriter().append("Email has been sent, please check your email.");
        }
        else {
            throw new UserNotFoundExeption();
        }

    }

    //Reset password with ID
    @RequestMapping(value = "/users/resetPassword/{userId}", method = RequestMethod.POST)
    public void resetForgottenPassword(@PathVariable("userId") long userId, @RequestBodyParam(name = "password") String password, HttpServletResponse response) throws Exception {

        User user = userRepository.findByUserId(userId);

        if (user != null) {
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            userRepository.save(user);

            new Thread( () -> {
                try {
                    MailUtil.sendMail(serverEmailAddress, user.getEmail(), "Password reset", MailUtil.PASSWORD_RESET);
                }
                catch (MessagingException e) {
                    //TODO Deal with email not sending
                }
            }).start();
            response.getWriter().append("Password reset successfully! \nRedirecting to login page...");
            //TODO Waits 5 seconds then redirects to login AND make the method in transaction
        }
        else {
            throw new UserNotFoundExeption();
        }
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
