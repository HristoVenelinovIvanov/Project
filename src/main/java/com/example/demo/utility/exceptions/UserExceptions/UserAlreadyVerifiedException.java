package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class UserAlreadyVerifiedException extends TechnoMarketException {

    public UserAlreadyVerifiedException() {
        super("User already verified!");
    }
}
