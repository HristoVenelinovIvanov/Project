package com.example.demo.utility.exceptions.OrderExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class InvalidAddressException extends TechnoMarketException {


    public InvalidAddressException(String msg) {
        super(msg);
    }
}
