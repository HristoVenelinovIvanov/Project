package com.example.demo.utility.validators;

import com.example.demo.model.dao.ProductCategoryDao;
import com.example.demo.model.pojo.Product;
import com.example.demo.utility.exceptions.ProductExceptions.*;
import com.example.demo.utility.exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductValidator {

    @Autowired
    private ProductCategoryDao productCategoryDao;

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
        if (product.getCategoryId() <= 0 || productCategoryDao.categoryExists(product.getCategoryId())) {
            throw new CategoryNotFoundException();
        }

        return true;
    }

    public HashMap<Integer, Product> editProduct(Product oldProduct, Product newProduct) {

        AtomicInteger fieldsChanged = new AtomicInteger(0);

            if(newProduct.getProductName() != null) {
                oldProduct.setProductName(newProduct.getProductName());
                fieldsChanged.getAndIncrement();
            }
            if (newProduct.getCategoryId() > 1 && !productCategoryDao.categoryExists(newProduct.getCategoryId())) {
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
            //TODO Add the other fields as editable
            HashMap<Integer, Product> mapped = new HashMap<>();
            mapped.put(fieldsChanged.intValue(), oldProduct);

            return mapped;
    }
}
