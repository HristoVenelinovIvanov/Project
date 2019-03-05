package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class ProductDoesNotExistException extends TechnoMarketException {


    public ProductDoesNotExistException(String msg) {
        super("No product available, sorry.");
    }
}
