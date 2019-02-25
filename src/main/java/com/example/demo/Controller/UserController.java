package com.example.demo.Controller;

import com.example.demo.Controller.Exceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public String registerUser(@RequestBody User user) {
        userRepository.save(user);
        return "You have registered successfully, please check your e-mail to verify your account!";
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void method(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/home");
        httpServletResponse.setStatus(302);
    }

}
