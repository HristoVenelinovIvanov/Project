package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class UserAlreadyVerifiedException extends TechnoMarketException {

    public UserAlreadyVerifiedException() {
        super("User already verified!");
    }
}
