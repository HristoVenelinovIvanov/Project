package com.example.demo.utility.exceptions.ValidationExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class EmailNotSentException extends TechnoMarketException {

    public EmailNotSentException(String msg) {
        super(msg);
    }
}
