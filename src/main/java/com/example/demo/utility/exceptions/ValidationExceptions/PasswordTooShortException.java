package com.example.demo.utility.exceptions.ValidationExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class PasswordTooShortException extends TechnoMarketException {

    public PasswordTooShortException() {
        super("The password must be at least 5 digits and must NOT contain any intervals");
    }
}
