package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.dto.ProductCategoryDTO;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.validators.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class ProductController extends BaseController{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductValidator productValidator;

    //Displaying all products
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> displayAllProducts() throws TechnoMarketException {

        List<Product> allProducts = productRepository.findAll();
        if(!allProducts.isEmpty()) {
            return allProducts;
        }
        throw new ProductDoesNotExistException("Sorry, No products available at the minute.");

    }

    //Searching an product that contains the given string
    @RequestMapping(value = "/products/{search}", method = RequestMethod.GET)
    public List<Product> search(@PathVariable(name = "search") String containsName) throws TechnoMarketException {

        if (productDao.productExists(containsName)) {
            return productRepository.findByProductNameContaining(containsName);
        }
        throw new ProductDoesNotExistException("Sorry, the product you are looking for does not exists.");
    }

    //Adding a product
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product, HttpSession session) throws TechnoMarketException {

        validateAdminLogin(session);
        if (productValidator.validateProduct(product)) {
            productRepository.save(product);
            return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
        }
       throw new TechnoMarketException("Oops, something went wrong on our side. :(");
    }

    //Deleting a product knowing its ID
    @RequestMapping(value = "/products/{productId}/delete", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("productId") long productId, HttpServletResponse response, HttpSession session) throws TechnoMarketException, IOException {

        if (validateAdminLogin(session)) {
            if (productDao.productExists(productId)) {
                Product product = productRepository.getOne(productId);
                productRepository.delete(product);
                response.getWriter().append("Product with ID: " + productId + " has been deleted from the system.");
            }
            else {
                throw new ProductDoesNotExistException("The product you are trying to delete does not exists!");
            }
        }
    }

    //Edit a product
    @RequestMapping(value = "/products/{productId}/edit", method = RequestMethod.POST)
    public Map<Integer, Product> editProduct(@PathVariable("productId") long productId, @RequestBody Product newProduct) throws TechnoMarketException {

        if (productDao.productExists(productId)) {
            Product editedProduct = productRepository.findByProductId(productId);
            TreeMap<Integer, Product> map = productValidator.editProduct(editedProduct, newProduct);
            productRepository.saveAndFlush(editedProduct);
            return map;
        }

        throw new ProductDoesNotExistException("The product you are trying to edit does not exists!");
    }

}
