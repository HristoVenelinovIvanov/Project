package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.github.lambdaexpression.annotation.RequestBodyParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class OrderController extends BaseController {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "products/{productId}/order", method = RequestMethod.POST)
    public String orderProduct(@PathVariable("productId") long productId,
                               @RequestBody Order orderDetails,
                               HttpSession session) throws Exception {
        validateLogin(session);
        System.out.println(orderDetails.getQuantity() + " " + orderDetails.getAddress());

        if (productDao.productExists(productId)) {
            if (productDao.checkQuantity(productId) >= orderDetails.getQuantity()) {
                User u = (User) session.getAttribute("userLogged");
                System.out.println(u);
                orderDetails.setUserId(u.getUserId());
                System.out.println(orderDetails.getUserId());
                System.out.println("going to save order");
                orderRepository.save(orderDetails);
                System.out.println("order saved");
                System.out.println(orderDetails.getQuantity() + " " +  productId);
                productDao.decreaseQuantity(orderDetails.getQuantity(), productId);
            }
        }
        else {
            throw new ProductDoesNotExistException("The product you are trying to buy does not exists!");
        }
        return "Everything is ok!";
    }
}
