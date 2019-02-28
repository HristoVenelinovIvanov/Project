package com.example.demo.Model.Utility.Exceptions.ValidationExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class ThereIsSpaceExeption extends TechnoMarketException {

    public ThereIsSpaceExeption(String msg) {
        super("The password must not has space/spaces in it!");
    }
}
