package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.dao.ProductCategoryDao;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.repository.ImageRepository;
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
import java.util.*;

@RestController
public class ProductController extends BaseController{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ImageRepository imageRepository;

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
    @RequestMapping(value = "/products/search/{search}", method = RequestMethod.GET)
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

        validateAdminLogin(session);

        Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findByProductId(productId));
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            response.getWriter().append("Product with ID: " + productId + " has been deleted from the system.");
            return;
        }
        throw new ProductDoesNotExistException("The product you are trying to delete does not exists!");
    }

    //Edit a product
    @RequestMapping(value = "/products/{productId}/edit", method = RequestMethod.POST)
    public Map<Integer, Product> editProduct(@PathVariable("productId") long productId, @RequestBody Product newProduct, HttpSession session) throws TechnoMarketException {

        validateAdminLogin(session);

        Optional<Product> product = Optional.ofNullable(productRepository.findByProductId(productId));
        synchronized (product) {
            if (product.isPresent()) {
                HashMap<Integer, Product> map = productValidator.editProduct(product.get(), newProduct);
                productRepository.saveAndFlush(product.get());
                return map;
            }
        }
        throw new ProductDoesNotExistException("The product you are trying to edit does not exists!");
    }

    @RequestMapping(value = "/products/laptops",method = RequestMethod.GET)
    public List<Product> allLaptops() throws ProductDoesNotExistException {

        List<Product> laptops = productRepository.findAllByCategoryId(15);

        if(laptops.isEmpty()) {
            throw new ProductDoesNotExistException("Sorry, no products in this category");
        }
        return laptops;
    }

    @RequestMapping(value = "/products/phones",method = RequestMethod.GET)
    public List<Map<String, Object>> allPhones() throws ProductDoesNotExistException {

        List<Map<String, Object>> phones = productCategoryDao.allPhones();

        if(phones.isEmpty()) {
            throw new ProductDoesNotExistException("Sorry, no products in this category");
        }

        return phones;
    }

    @RequestMapping(value = "/products/filter/", method = RequestMethod.GET)
    public List<Map<String, Object>> takeFilteredFromDB(
            @RequestParam(value = "product_name", required = false) String productName,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "discounted", required = false) String discounted,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "inches", required = false) String inches,
            @RequestParam(value = "frequency", required = false) String frequency,
            @RequestParam(value = "kw", required = false) String kw,
            @RequestParam(value = "number_of_hobs", required = false) String numberOfHobs,
            @RequestParam(value = "timer", required = false) String timer,
            @RequestParam(value = "camera_pixels", required = false) String cameraPixels,
            @RequestParam(value = "finger_print", required = false) String fingerPrint,
            @RequestParam(value = "water_proof", required = false) String waterProof,
            @RequestParam(value = "kg_capacity", required = false) String kgCapacity,
            @RequestParam(value = "intelligent_wash", required = false) String intelligentWash,
            @RequestParam(value = "inbuild_dryer", required = false) String inbuildDryer,
            @RequestParam(value = "lowerThan", required = false) String lowerThan,
            @RequestParam(value = "higherThan", required = false) String higherThan) {

        return productDao.filterProducts(productName, price, discounted, brand, inches, frequency, kw, numberOfHobs,timer,
                cameraPixels, fingerPrint, waterProof, kgCapacity, intelligentWash, inbuildDryer, lowerThan, higherThan);
    }

    @RequestMapping(value = "/products/{productId}/addImage/{imageId}", method = RequestMethod.GET)
    public void addImageToProduct(@PathVariable("productId") long productId, @PathVariable("imageId") long imageId, HttpSession session, HttpServletResponse response) throws Exception {
        validateAdminLogin(session);

        if (productDao.productExists(productId)) {
            imageRepository.addImageToProduct(productId, imageId);
            response.getWriter().append("You have successfully set image with ID: " + imageId + " to product with ID: " + productId);
            return;
        }
        throw new TechnoMarketException("Product id or image id does not exists!");
    }

    @RequestMapping(value = "/products/discount", method = RequestMethod.GET)
    public List<Product> showDiscounted() {

        return productRepository.findAllByDiscounted();

    }

}
