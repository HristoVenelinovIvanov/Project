package com.example.demo.utility.exceptions.ProductExceptions;

import com.example.demo.utility.exceptions.TechnoMarketException;

public class CategoryNotFoundException extends TechnoMarketException {


    public CategoryNotFoundException() {
        super("Category not found.");
    }
}
