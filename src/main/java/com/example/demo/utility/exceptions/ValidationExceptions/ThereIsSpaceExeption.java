package com.example.demo.utility.exceptions.ValidationExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class ThereIsSpaceExeption extends TechnoMarketException {

    public ThereIsSpaceExeption(String msg) {
        super("The password must not has space/spaces in it!");
    }
}
