package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class UserNotFoundExeption extends TechnoMarketException {

    public UserNotFoundExeption() {
        super("User with this credentials does not exists!");
    }
}
