package com.example.demo.Controller;

import com.example.demo.Model.DAO.ProductDao;
import com.example.demo.Model.POJO.Product;
import com.example.demo.Model.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Product> displayAllProducts() {
        return productRepository.findAll();
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product) {
        productDao.addProduct(product);
        return "Product added successfully in category " + product.getCategoryId() + " with ID: " + product.getProductId();
    }



}
