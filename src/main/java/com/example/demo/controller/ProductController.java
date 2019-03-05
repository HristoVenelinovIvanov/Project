package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
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
    public List<Product> displayAllProducts(HttpSession session) throws TechnoMarketException {
            return productRepository.findAll();

    }

    //Searching the product with JSON Body parameters, we could possibly change it to pathvar params
    @RequestMapping(value = "/products/{search}", method = RequestMethod.GET)
    public List<Product> search(@PathVariable(name = "search") String containsName, HttpSession session) throws TechnoMarketException {

        if (productDao.productExists(containsName)) {
            return productRepository.findByProductNameContaining(containsName);
        }

        throw new ProductDoesNotExistException("Sorry, the product you are looking for does not exists.");
    }

    //Adding a product with validation
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product, HttpSession session) throws TechnoMarketException {
        validateLogin(session);

            //if user not logged, adminValidator returns false
            if (productValidator.validateProduct(product)) {
                productRepository.save(product);
                return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
            }

       throw new TechnoMarketException("Oops, something went wrong on our side. :(");
    }

    //Deleting a product knowing its ID
    @RequestMapping(value = "/products/delete/{productId}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("productId") long productId, HttpServletResponse response, HttpSession session) throws TechnoMarketException, IOException {

        User u = (User) session.getAttribute("loggedUser");

        if (userValidator.isAdmin(u)) {
            if (productDao.productExists(productId)) {
                Product product = productRepository.getOne(productId);
                productRepository.delete(product);
                response.getWriter().append("Product with ID: " + productId + " has been deleted from the system.");
            }
            else {
                throw new ProductDoesNotExistException("Sorry the product does not exists");
            }
        }
    }


}
