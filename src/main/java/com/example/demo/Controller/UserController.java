package com.example.demo.Controller;

import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Utility.MailUtil;
import com.example.demo.Model.Validators.UserValidator;
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

    Map<Integer, Enumeration<Product>> productsInCart = new HashMap<>();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    //99% finished
    public void registerUser(@RequestBody User user, HttpServletResponse response) throws Exception {
        if (userValidator.validateEmptyFields(user)) {
            if (userRepository.findByEmail(user.getEmail()) == null) {
                userRepository.save(user);
                new Thread( () -> {
                    try {
                        MailUtil.sendMail("technomarket.project@gmail.com", user.getEmail(), "Account verification", "Please blink 5 times to verify your account");
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

    //show user by id
    @RequestMapping (value = "/users/{userId}", method = RequestMethod.GET)
        public User showAllUsers(@PathVariable("userId") long userId, HttpSession session){
            User user = userRepository.findByUserId(userId);
            session.setAttribute("userId", user);
            return user;
    }

    //login user
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User logIn(@RequestBody User user, HttpSession session) throws TechnoMarketException {
        User u = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        //NOT FINISHED
        if (new UserValidator().validateLoginFields(user)) {
            if (u != null) {
                session.setAttribute("userLogged", u);
                return u;
            }
            throw new UserNotFoundExeption();
        }
        throw new UserNotFoundExeption();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public void logOut (HttpSession session, HttpServletResponse response) throws IOException {
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



    // the isLoged is implemented in BaseControler as validateLogin

//    public static boolean isLoged(HttpServletRequest req){
//        HttpSession session = req.getSession();
//        if (session.isNew() || session.getAttribute("logged") == null ||
//                session.getAttribute("logged").equals(false)){
//            return false;
//        }
//        return true;
//    }


}
