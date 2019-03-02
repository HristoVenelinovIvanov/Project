package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class InvalidProductName extends TechnoMarketException {
    public InvalidProductName() {
        super("Please enter a valid product name!");
    }
}
