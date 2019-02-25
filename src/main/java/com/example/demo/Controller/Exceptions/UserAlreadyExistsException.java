package com.example.demo.Controller.Exceptions;

public class UserAlreadyExistsException extends Exception {


    @Override
    public String getMessage() {
        return "User with this username/email already exists!";
    }
}
