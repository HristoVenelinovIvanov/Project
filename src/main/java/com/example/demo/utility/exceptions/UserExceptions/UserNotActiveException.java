package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UserNotActiveException extends TechnoMarketException {
    public UserNotActiveException() {
        super("This account is not verified. \nPlease verify your account!");
    }
}
