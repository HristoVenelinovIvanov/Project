package com.example.demo.Controller;

import com.example.demo.Model.DTO.UserRegisterDTO;
import com.example.demo.Model.Utility.Exceptions.UserAlreadyExistsException;
import com.example.demo.Model.Repository.UserRepository;
import com.example.demo.Model.POJO.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController extends BaseController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public String registerUser(@RequestBody UserRegisterDTO user) {
        //validate user

       // userRepository.save(user);

       // sendEmail(user.getEmail());
        return "You have registered successfully, please check your e-mail to verify your account!";
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void method(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location Redirect", "/home");
        httpServletResponse.setStatus(302);
    }



}
