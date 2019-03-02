package com.example.demo.Model.Utility.Exceptions;

public class UserNotFoundExeption extends TechnoMarketException {

    public UserNotFoundExeption() {
        super("Sorry, user not found");
    }
}
