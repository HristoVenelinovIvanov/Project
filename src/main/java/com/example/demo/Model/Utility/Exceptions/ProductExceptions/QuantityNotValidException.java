package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class QuantityNotValidException extends TechnoMarketException {
    public QuantityNotValidException(String msg) {
        super(msg);
    }
}
