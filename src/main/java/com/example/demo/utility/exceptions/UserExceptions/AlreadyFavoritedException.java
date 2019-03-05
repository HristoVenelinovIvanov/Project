package com.example.demo.utility.exceptions.UserExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class AlreadyFavoritedException extends TechnoMarketException {
    public AlreadyFavoritedException() {
        super("Product already in favorites!");
    }
}
