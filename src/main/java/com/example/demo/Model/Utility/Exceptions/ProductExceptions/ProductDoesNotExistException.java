package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class ProductDoesNotExistException extends TechnoMarketException {


    public ProductDoesNotExistException() {
        super("No product available, sorry.");
    }
}
