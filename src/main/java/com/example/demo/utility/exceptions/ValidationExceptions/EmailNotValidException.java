package com.example.demo.utility.exceptions.ValidationExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class EmailNotValidException extends TechnoMarketException {


    public EmailNotValidException() {
        super("The entered email is not a valid email!");
    }
}
