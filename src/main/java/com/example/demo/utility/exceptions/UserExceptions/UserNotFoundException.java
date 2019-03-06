package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UserNotFoundException extends TechnoMarketException {

    public UserNotFoundException() {
        super("User with this credentials does not exists!");
    }
}
