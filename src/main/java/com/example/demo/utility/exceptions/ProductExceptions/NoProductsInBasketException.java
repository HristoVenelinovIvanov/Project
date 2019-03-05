package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class NoProductsInBasketException extends TechnoMarketException {

    public NoProductsInBasketException() {

        super("No products in the basket!");
    }
}
