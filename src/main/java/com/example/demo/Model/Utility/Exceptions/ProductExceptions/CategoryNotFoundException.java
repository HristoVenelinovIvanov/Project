package com.example.demo.Model.Utility.Exceptions.ProductExceptions;

import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;

public class CategoryNotFoundException extends TechnoMarketException {


    public CategoryNotFoundException() {
        super("Category not found.");
    }
}
