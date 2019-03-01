package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class UsersNotAvailableException extends TechnoMarketException {


    public UsersNotAvailableException() {
        super("Users not available, sorry :(");
    }
}
