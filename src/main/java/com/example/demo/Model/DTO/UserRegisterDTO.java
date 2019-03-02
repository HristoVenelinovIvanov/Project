package com.example.demo.Model.DTO;

import com.example.demo.Model.Enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
    private Gender gender;


}
