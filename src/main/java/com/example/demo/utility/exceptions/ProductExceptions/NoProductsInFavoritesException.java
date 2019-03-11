package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class NoProductsInFavoritesException extends TechnoMarketException {
    public NoProductsInFavoritesException() {
        super("You currently have no products in favorites.");
    }
}
