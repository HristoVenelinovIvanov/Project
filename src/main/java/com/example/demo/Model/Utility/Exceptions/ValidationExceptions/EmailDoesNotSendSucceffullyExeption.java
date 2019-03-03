package com.example.demo.Model.Utility.Exceptions.ValidationExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class EmailDoesNotSendSucceffullyExeption extends TechnoMarketException {

    public EmailDoesNotSendSucceffullyExeption(String msg) {
        super(msg);
    }
}
