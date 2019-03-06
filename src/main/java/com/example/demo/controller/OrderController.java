package com.example.demo.controller;

import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.OrderDetails;
import com.example.demo.model.pojo.User;
import com.example.demo.model.pojo.UserAddress;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class OrderController extends BaseController {

    @Autowired
    private ProductDao productDao;

    @RequestMapping(value = "products/{productId}/order", method = RequestMethod.GET)
    public String orderProduct(@PathVariable("productId") long productId, @RequestBody OrderDetails orderDetails, HttpSession session, UserAddress userAddress, HttpServletResponse response) throws Exception {
        validateLogin(session);

        if (productDao.productExists(productId)) {
            if (productDao.checkQuantity(productId) > orderDetails.getProductQuantity()) {
//
//                orderDetails.setUserId((long) session.getAttribute("userId"));
//                orderRepository.save(orderDetails);
//                return OrderMessage.orderMessage(
//                        orderDetails,
//                        (User) session.getAttribute("userId"),
//                        userAddress);
            }
            else {
                response.getWriter().append("Sorry, we do not have enough of that product in the store.");
            }
        }
        else {
            throw new ProductDoesNotExistException("The product you are trying to buy does not exists!");
        }
        return "Oops, something went wrong :(";
    }
}
