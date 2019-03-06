package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class CategoryAlreadyExistsException extends TechnoMarketException {

    public CategoryAlreadyExistsException() {
        super("Category already Exists");
    }
}
