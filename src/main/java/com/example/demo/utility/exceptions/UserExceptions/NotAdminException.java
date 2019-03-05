package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class NotAdminException extends TechnoMarketException {


    public NotAdminException(String msg) {
        super(msg);
    }
}
