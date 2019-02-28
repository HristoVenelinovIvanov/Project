package com.example.demo.Controller;

import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.UserNotFoundExeption;
import com.example.demo.Model.Validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    //99% finished
    public void registerUser(@RequestBody User user) throws TechnoMarketException {
        if(userRepository.findByEmail(user.getEmail()) == null && new UserValidator().validateEmptyFields(user)) {
            userRepository.save(user);
        }
        else {
            throw new UserAlreadyExistsException();
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User logIn(@RequestBody User user, HttpSession session) throws TechnoMarketException{

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
