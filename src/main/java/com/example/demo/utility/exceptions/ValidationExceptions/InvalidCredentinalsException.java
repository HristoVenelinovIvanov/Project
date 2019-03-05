package com.example.demo.utility.exceptions.ValidationExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class InvalidCredentinalsException extends TechnoMarketException {

    public InvalidCredentinalsException(String msg) {
        super(msg);
    }
}
