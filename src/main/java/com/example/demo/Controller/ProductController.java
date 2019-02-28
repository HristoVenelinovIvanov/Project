package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

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
    public String addProduct(@RequestBody Product product) {
        productDao.addProduct(product);
        return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
    }

    @RequestMapping(value = "/add/cart")
    public void addToCart(RequestParam username){
        //TODO add cart product to cart
    }

}
