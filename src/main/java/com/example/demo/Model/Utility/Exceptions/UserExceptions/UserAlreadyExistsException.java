package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class UserAlreadyExistsException extends TechnoMarketException {


    public UserAlreadyExistsException() {
        super("User already exists");
    }

}
