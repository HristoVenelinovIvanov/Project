package com.example.demo.controller;

import com.example.demo.model.dao.OrderedProductsDao;
import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.dao.UserOrdersDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.utility.ShoppingCart;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.exceptions.TechnoMarketException;
import com.example.demo.utility.ordermessages.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShoppingCartController extends BaseController{

    @Autowired
    private ShoppingCart shoppingCart;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderedProductsDao orderedProductsDao;
    @Autowired
    private UserOrdersDao userOrdersDao;

    //Returns items in the cart
    @RequestMapping(value = "/shoppingCart", method = RequestMethod.GET)
    public Map<String, List<Product>> shoppingCart(HttpSession session) throws TechnoMarketException {
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
    @RequestMapping(value = "/shoppingCart/addProduct/{productId}", method = RequestMethod.POST)
    @Transactional
    public void addProductToCart(@PathVariable("productId") long productId, HttpSession session, HttpServletResponse response) throws Exception {

        validateLogin(session);

        Optional<Product> optionalProduct = Optional.ofNullable(productRepository.findByProductId(productId));

        if (optionalProduct.isPresent()) {
            if (productDao.checkQuantity(optionalProduct.get().getProductId()) >= 1) {
                Product product = productRepository.findByProductId(productId);
                productDao.orderDecreaseQuantity(1, product);
                response.getWriter().append(shoppingCart.addProductToCart(product));
                return;
            }
        }
            throw new ProductDoesNotExistException("The product you are trying to add is out of stock, or does not exists!");
    }

    //Removes a product from the cart and returns an successful/not successful response
    @RequestMapping(value = "/shoppingCart/removeProduct/{productInCartNumber}", method = RequestMethod.GET)
    public void removeProductFromCart(@PathVariable("productInCartNumber") int productInCartNumber, HttpSession session, HttpServletResponse response) throws Exception {

        validateLogin(session);
        shoppingCart.removeProductFromCart(productInCartNumber, response);

    }

    @RequestMapping(value = "/shoppingCart/checkout", method = RequestMethod.GET)
    @Transactional
    public void checkout(HttpSession session, HttpServletResponse response) throws TechnoMarketException, IOException {
        validateLogin(session);

        if(shoppingCart.getShoppingCart().isEmpty()) {
            response.getWriter().append("Nothing to checkout");
            return;
        }
        else {
            User user = (User) session.getAttribute("userLogged");
            Order order = orderRepository.save(new Order(ShoppingCart.ORDER_NOT_CONFIRMED,
                                                         user.getUserId(),
                                                         shoppingCart.getShoppingCart().size(),
                                                         ShoppingCart.ORDER_NOT_CONFIRMED,
                                                         ShoppingCart.ORDER_NOT_CONFIRMED));

            Map<Product, Integer> productAndQuantity = new HashMap<>();

            for (Product p : shoppingCart.getShoppingCart()) {
                if (productAndQuantity.containsKey(p)) {
                    productAndQuantity.merge(p, 1, Integer::sum);
                }
                else {
                    productAndQuantity.put(p, 1);
                }
            }

            for (Map.Entry<Product, Integer> entry : productAndQuantity.entrySet()) {
                orderedProductsDao.addToOrdered(order.getOrderId(), entry.getKey().getProductId(), entry.getValue());
            }
            userOrdersDao.addToUserOrders(order.getOrderId(), user.getUserId());
            response.getWriter().append(OrderMessage.notConfirmedOrderMessage(order, user.getEmail()));
        }
    }

}

