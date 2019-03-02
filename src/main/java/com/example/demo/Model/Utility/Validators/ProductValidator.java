package com.example.demo.Model.Utility.Validators;

import com.example.demo.Model.DTO.ProductCategoryDTO;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Utility.Exceptions.ProductExceptions.*;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    @Autowired
    private ProductCategoryDTO productCategoryDTO;

    public boolean validateProduct(Product product) throws TechnoMarketException {

        if (product.getProductName().isEmpty() || product.getProductName() == null) {
            throw new InvalidProductName();
        }
        if (product.getPrice() < 0.00) {
            throw new PriceNotSetException();
        }
        if (product.getQuantity() < 0) {
            throw new QuantityNotValidException("Please enter valid product quantity!");
        }
        if (product.getCategoryId() < 0 || productCategoryDTO.categories(product.getCategoryId())) {
            throw new CategoryNotFoundException();
        }
        if (product.getQuantityOnOrder() < 0) {
            throw new QuantityNotValidException("Cannot be ordered more than the quantity we have");
        }
        if (product.getDiscounted() < 0) {
            throw new InvalidDiscountPriceException("Invalid discount price entered");
        }

        return true;
    }
}
