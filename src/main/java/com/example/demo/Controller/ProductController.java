package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Repository.ProductRepository;
import com.example.demo.Model.Utility.Exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.Model.Utility.Exceptions.TechnoMarketException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotAdminException;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotLoggedException;
import com.example.demo.Model.Utility.Validators.AdminValidator;
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
    @Autowired
    private AdminValidator adminValidator;


    //Displaying all products
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public List<Product> displayAllProducts(HttpSession session) throws TechnoMarketException {
        User u = (User) session.getAttribute("userLooged");
        if (adminValidator.isAdmin(u)){
            return productRepository.findAll();
        }
        throw new NotLoggedException("You are not logged");
    }

    //Searching the product with JSON Body parameters, we could possibly change it to pathvar params
    @RequestMapping(value = "/products/", method = RequestMethod.GET)
    public List<Product> displayProductByName(@RequestBodyParam(name = "contains") String containsName, HttpSession session) throws TechnoMarketException {
        User u = (User) session.getAttribute("userLogged");
        if (adminValidator.isAdmin(u)){
            //TODO if product doesn't exist throw ProductNotExistException
        }
        return productRepository.findByProductNameContaining(containsName);
    }

    //Adding a product with validation
    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product, HttpSession session, HttpServletResponse response) throws TechnoMarketException {
        User u = (User) session.getAttribute("userLogged");
        if (adminValidator.isAdmin(u)) {
            //if user not logged, adminValidator returns false
            if (productValidator.validateProduct(product)) {
                productRepository.save(product);
                return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
            }
        }
        response.setStatus(500);
       throw new TechnoMarketException("Oops, something went wrong on our side. :(");
    }

    //Deleting a product knowing its ID
    @RequestMapping(value = "/products/delete/{productId}", method = RequestMethod.DELETE)
    public void deleteProduct(@PathVariable("productId") long productId, HttpServletResponse response, HttpSession session) throws TechnoMarketException, IOException {
        User u = (User) session.getAttribute("loggedUser");
        if (adminValidator.isAdmin(u)) {
            if (productDao.productExists(productId)) {
                Product product = productRepository.getOne(productId);
                productRepository.delete(product);
                response.getWriter().append("Product with ID: " + productId + " has been deleted from the system.");
            } else {
                throw new ProductDoesNotExistException("Sorry the product does not exists");
            }
        }
    }

    @RequestMapping(value = "/add/cart")
    public void addToCart(RequestParam username){
        //TODO add cart product to cart
    }

}
