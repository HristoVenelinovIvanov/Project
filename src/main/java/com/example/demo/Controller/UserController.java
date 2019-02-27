package com.example.demo.Controller;

import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;


    Map<Integer, Enumeration<Product>> productsInCart = new HashMap<>();

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public String registerUser(@RequestBody User user) {
        //validate user
        userRepository.save(user);

       // sendEmail(user.getEmail());
        return "You have registered successfully, please check your e-mail to verify your account!";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User logIn(@RequestBody User user, HttpSession session) {
        if (userDao.checkIfUserExists(user.getUsername(), user.getPassword())) {
            session.setAttribute("userLogged", user);
        }
        return user;
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

    @RequestMapping(value = "/add/cart")
    public void addToCart(HttpServletRequest req, HttpServletResponse resp){
        HttpSession session = req.getSession();
        Integer accessCount = (Integer) session.getAttribute("accessCount");
        Enumeration atributes = req.getAttributeNames();
        productsInCart.put(accessCount, atributes);
        for (Map.Entry<Integer, Enumeration<Product>> p : productsInCart.entrySet()){
            System.out.println(p);
        }
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
