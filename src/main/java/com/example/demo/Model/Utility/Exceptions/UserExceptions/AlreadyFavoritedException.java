package com.example.demo.Model.Utility.Exceptions.UserExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class AlreadyFavoritedException extends TechnoMarketException {
    public AlreadyFavoritedException() {
        super("Product already in favorites!");
    }
}
