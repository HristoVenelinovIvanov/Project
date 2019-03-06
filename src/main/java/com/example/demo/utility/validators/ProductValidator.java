package com.example.demo.utility.validators;

import com.example.demo.model.dto.ProductCategoryDTO;
import com.example.demo.model.pojo.Product;
import com.example.demo.utility.exceptions.ProductExceptions.*;
import com.example.demo.utility.exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

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
        if (product.getQuantity() <= 0) {
            throw new QuantityNotValidException("Please enter valid product quantity!");
        }
        if (product.getCategoryId() <= 0 || productCategoryDTO.categoryExists(product.getCategoryId())) {
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

    public TreeMap<Integer, Product> editProduct(Product oldProduct, Product newProduct) {

        AtomicInteger fieldsChanged = new AtomicInteger(0);

            if(newProduct.getProductName() != null) {
                oldProduct.setProductName(newProduct.getProductName());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getCategoryId() > 0 && !productCategoryDTO.categoryExists(newProduct.getCategoryId())) {
                oldProduct.setCategoryId(newProduct.getCategoryId());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getPrice() > 0) {
                oldProduct.setPrice(newProduct.getPrice());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getQuantity() > 0) {
                oldProduct.setQuantity(oldProduct.getQuantity() + newProduct.getQuantity());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getDiscounted() > 0) {
                oldProduct.setDiscounted(newProduct.getDiscounted());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getCharacteristics() != null) {
                oldProduct.setCharacteristics(newProduct.getCharacteristics());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getProductImage() != null) {
                oldProduct.setProductImage(newProduct.getProductImage());
                fieldsChanged.getAndIncrement();
            }

            TreeMap<Integer, Product> mapped = new TreeMap<>();
            mapped.put(fieldsChanged.intValue(), oldProduct);

            return mapped;
    }
}
