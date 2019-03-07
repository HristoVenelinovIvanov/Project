package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.ordermessages.OrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class OrderController extends BaseController {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    //IN PROGRESS, ALMOST WORKING
    @RequestMapping(value = "/products/{productId}/order", method = RequestMethod.POST)
    public void orderProduct(@PathVariable("productId") long productId, @RequestBody Order order, HttpSession session, HttpServletResponse response) throws Exception {
        validateLogin(session);

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            User user = (User) session.getAttribute("userLogged");

            if (productDao.checkQuantity(productId) >= order.getQuantity()) {
                productDao.orderDecreaseQuantity(order.getQuantity(), productOptional.get());
                order.setUserId((long) session.getAttribute("userId"));
                orderRepository.save(order);
                response.getWriter().append(OrderMessage.orderMessage(order, user.getEmail()));
            }
            else {
                response.getWriter().append("We don't have enough in stock of product with ID " + productId);
            }
        }
        else {
            throw new ProductDoesNotExistException("The product you are trying to buy does not exists!");
        }
    }
}
