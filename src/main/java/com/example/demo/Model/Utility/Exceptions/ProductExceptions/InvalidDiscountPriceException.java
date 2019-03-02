package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class InvalidDiscountPriceException extends TechnoMarketException {


    public InvalidDiscountPriceException(String msg) {
        super(msg);
    }
}
