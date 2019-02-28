package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Repository.ProductRepository;
import com.example.demo.Model.Utility.Exceptions.UserExceptions.NotLoggedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class ProductController extends BaseController{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;


    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public @ResponseBody List<Product> displayAllProducts(@RequestParam(value="product_id", required = true) Integer product_id) {
        return productRepository.findAll();
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product, HttpSession session, Exception e) throws NotLoggedException {
        validateLogin(session, e);
        productDao.addProduct(product);
        return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
    }

    @RequestMapping(value = "/add/cart")
    public void addToCart(RequestParam username){
        //TODO add cart product to cart
    }

}
