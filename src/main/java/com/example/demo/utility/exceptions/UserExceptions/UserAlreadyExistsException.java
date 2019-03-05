package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UserAlreadyExistsException extends TechnoMarketException {


    public UserAlreadyExistsException() {
        super("User already exists");
    }

}
