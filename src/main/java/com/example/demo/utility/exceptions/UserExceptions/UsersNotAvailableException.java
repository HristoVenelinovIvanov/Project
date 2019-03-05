package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UsersNotAvailableException extends TechnoMarketException {


    public UsersNotAvailableException() {
        super("Users not available, sorry :(");
    }
}
