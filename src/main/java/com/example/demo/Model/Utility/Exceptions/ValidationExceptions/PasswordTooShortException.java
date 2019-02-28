package com.example.demo.Model.Utility.Exceptions.ValidationExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class PasswordTooShortException extends TechnoMarketException {

    public PasswordTooShortException() {
        super("The password must be at least 5 digits");
    }
}
