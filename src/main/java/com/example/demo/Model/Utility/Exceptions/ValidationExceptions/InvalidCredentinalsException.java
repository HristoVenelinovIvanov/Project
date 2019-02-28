package com.example.demo.Model.Utility.Exceptions.ValidationExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class InvalidCredentinalsException extends TechnoMarketException {

    public InvalidCredentinalsException(String msg) {
        super(msg);
    }
}
