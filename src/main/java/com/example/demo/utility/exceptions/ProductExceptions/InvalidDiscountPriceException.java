package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class InvalidDiscountPriceException extends TechnoMarketException {


    public InvalidDiscountPriceException(String msg) {
        super(msg);
    }
}
