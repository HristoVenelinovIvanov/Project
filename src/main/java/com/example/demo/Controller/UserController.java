package com.example.demo.Controller;

import com.example.demo.Model.Utility.Exceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;


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


}
