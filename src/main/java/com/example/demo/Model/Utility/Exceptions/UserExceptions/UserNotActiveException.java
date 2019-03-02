package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class UserNotActiveException extends TechnoMarketException {
    public UserNotActiveException() {
        super("This account is not verified. \nPlease verify your account!");
    }
}
