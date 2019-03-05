package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UserNotFoundExeption extends TechnoMarketException {

    public UserNotFoundExeption() {
        super("User with this credentials does not exists!");
    }
}
