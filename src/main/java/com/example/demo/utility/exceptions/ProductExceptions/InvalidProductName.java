package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class InvalidProductName extends TechnoMarketException {
    public InvalidProductName() {
        super("Please enter a valid product name!");
    }
}
