package com.example.demo.Model.Utility.Exceptions.ValidationExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class EmailNotValidException extends TechnoMarketException {


    public EmailNotValidException() {
        super("The entered email is not a valid email!");
    }
}
