package com.example.demo.utility.exceptions.OrderExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class NoOrdersAvailableException extends TechnoMarketException {

    public NoOrdersAvailableException() {
        super("No orders available");
    }
}
