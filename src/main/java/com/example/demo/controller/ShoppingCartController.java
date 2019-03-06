package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Product;
import com.example.demo.utility.ShoppingCart;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class ShoppingCartController extends BaseController{

    @Autowired
    private ShoppingCart shoppingCart;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;

    //Returns items in the cart
    @RequestMapping(value = "/shoppingCart", method = RequestMethod.GET)
    public List<Product> shoppingCart(HttpSession session) throws TechnoMarketException {
        validateLogin(session);
        return shoppingCart.viewProducts();
    }

    //Returns number of items in the cart
    @RequestMapping(value = "/shoppingCart/size", method = RequestMethod.GET)
    public String countOfItemsInCart(HttpSession session) throws TechnoMarketException {
        validateLogin(session);
        return "You currently have " + shoppingCart.getItemCount() + " items in your cart";
    }

    //Adds a product to cart and returns an successful/not successful response
    @RequestMapping(value = "/shoppingCart/addProduct/{productId}", method = RequestMethod.GET)
    public void addProductToCart(@PathVariable("productId") long productId, HttpSession session, HttpServletResponse response) throws Exception {

        validateLogin(session);
        if (productDao.productExists(productId)) {

            Product product = productRepository.findByProductId(productId);
            response.getWriter().append(shoppingCart.addProductToCart(product));

        }
        else {
            throw new ProductDoesNotExistException("The product you are trying to add does not exists!");
        }

    }

    //Removes a product from the cart and returns an successful/not successful response
    @RequestMapping(value = "/shoppingCart/removeProduct/{productInCartNumber}", method = RequestMethod.GET)
    public void removeProductFromCart(@PathVariable("productInCartNumber") int productInCartNumber, HttpSession session, HttpServletResponse response) throws Exception {

        validateLogin(session);
        shoppingCart.removeProductFromCart(productInCartNumber, response);

    }

    @RequestMapping(value = "/shoppingCart/checkout", method = RequestMethod.GET)
    public void checkout() {
        //TODO Implement this when order model is finished
    }
}

