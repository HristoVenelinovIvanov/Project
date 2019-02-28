package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class NotAdminException extends TechnoMarketException {


    public NotAdminException(String msg) {
        super(msg);
    }
}
