package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class QuantityNotValidException extends TechnoMarketException {
    public QuantityNotValidException(String msg) {
        super(msg);
    }
}
