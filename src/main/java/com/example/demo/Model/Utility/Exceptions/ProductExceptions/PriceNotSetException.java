package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class PriceNotSetException extends TechnoMarketException {
    public PriceNotSetException() {
        super("Please specify an valid price for the product!");
    }
}
