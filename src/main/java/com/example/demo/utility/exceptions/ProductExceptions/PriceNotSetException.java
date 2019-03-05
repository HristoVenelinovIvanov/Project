package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class PriceNotSetException extends TechnoMarketException {
    public PriceNotSetException() {
        super("Please specify an valid price for the product!");
    }
}
