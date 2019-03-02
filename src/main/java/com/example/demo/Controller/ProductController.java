package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Repository.ProductRepository;
import com.example.demo.Model.Utility.Exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Validators.ProductValidator;
import com.github.lambdaexpression.annotation.RequestBodyParam;
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
    public List<Product> displayAllProducts() {
        //TODO Validate if admin and logged else code 405 if no products throw ProductNotExistException
        return productRepository.findAll();
    }

    //Searching the product with JSON Body parameters, we could possibly change it to pathvar params
    @RequestMapping(value = "/products/", method = RequestMethod.GET)
    public List<Product> displayProductByName(@RequestBodyParam(name = "contains") String containsName) {
        //TODO Validate if logged else 405 and if product doesn't exist throw ProductNotExistException
        return productRepository.findByProductNameContaining(containsName);
    }

    //Adding a product with validation
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product, HttpSession session, HttpServletResponse response) throws TechnoMarketException {
        //validateLogin(session, e);// TODO Validate if admin and logged else code 405

        if (productValidator.validateProduct(product)) {
            productRepository.save(product);
            return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
        }

        response.setStatus(500);
       throw new TechnoMarketException("Oops, something went wrong on our side. :(");
    }

    //Deleting a product knowing its ID
    @RequestMapping(value = "/products/delete/{productId}", method = RequestMethod.DELETE)
        public void deleteProduct(@PathVariable("productId") long productId, HttpServletResponse response) throws ProductDoesNotExistException, IOException {
        //TODO Validate if logged and admin
        if (productDao.productExists(productId)) {

            Product product = productRepository.getOne(productId);
            productRepository.delete(product);
            response.getWriter().append("Product with ID: " + productId + " has been deleted from the system.");
        }
        else {
            throw new ProductDoesNotExistException();
        }
    }

    @RequestMapping(value = "/add/cart")
    public void addToCart(RequestParam username){
        //TODO add cart product to cart
    }

}
