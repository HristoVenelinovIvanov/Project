package com.example.demo.controller;

import com.example.demo.model.dao.OrderedProductsDao;
import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.dao.UserOrdersDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.OrderExceptions.InvalidAddressException;
import com.example.demo.utility.exceptions.OrderExceptions.NoOrdersAvailableException;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.mail.MailUtil;
import com.example.demo.utility.ordermessages.OrderMessage;
import com.github.lambdaexpression.annotation.RequestBodyParam;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController extends BaseController {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderedProductsDao orderedProductsDao;
    @Autowired
    private UserOrdersDao userOrdersDao;


    //Ordering only one product
    @RequestMapping(value = "/products/{productId}/order", method = RequestMethod.POST)
    @Transactional
    public void orderProduct(@PathVariable("productId") long productId, @RequestBody Order order, HttpSession session, HttpServletResponse response) throws Exception {
        validateLogin(session);

        Optional<Product> productOptional = productRepository.findById(productId);

        //Checks if the product is available and in stock
        if (productOptional.isPresent() && productOptional.get().getQuantity() >= order.getQuantity()) {
            User user = (User) session.getAttribute("userLogged");

                productDao.orderDecreaseQuantity(order.getQuantity(), productOptional.get());
                order.setUserId(user.getUserId());
                orderRepository.save(order);
                userOrdersDao.addToUserOrders(order.getOrderId(), user.getUserId());
                orderedProductsDao.addToOrdered(order.getOrderId(), productOptional.get().getProductId(), order.getQuantity());

                new Thread(() -> {
                    try {
                        MailUtil.sendMail(serverEmailAddress,user.getEmail(),
                                "Order confirmation",
                                MailUtil.ORDER_CONFIRMATION_MESSAGE + order.toString() + OrderMessage.productMessage(productOptional.get(),order));
                    } catch (MessagingException e) {
                        log.log(Priority.WARN, e.getMessage(), e);
                    }
                }).start();

                response.getWriter().append(OrderMessage.orderMessage(order, user.getEmail()));
        }
        else {
            throw new ProductDoesNotExistException("The product you are trying to buy is either out of stock, or does not exists!");
        }
    }

    @RequestMapping(value = "/orders/{orderId}/confirm", method = RequestMethod.POST)
    @Transactional
    public void confirmOrder(@PathVariable("orderId") long orderId, @RequestBodyParam String address, HttpSession session, HttpServletResponse response) throws Exception {

        validateLogin(session);
        User user = (User) session.getAttribute("userLogged");

        if (address.isEmpty() || address == null) {
            throw new InvalidAddressException("The address field is empty or incorrect!");
        }

        if (!user.getOrders().isEmpty() && orderRepository.existsById(orderId)) {
            for (Order order : user.getOrders()) {
                if (order.getOrderId() == orderId && order.getStatus().contentEquals(Order.IS_ORDER_CONFIRMED)) {
                    order.setAddress(address);
                    order.setStatus("Not shipped");
                    order.setRequiredDate(LocalDate.now().plus(4, ChronoUnit.DAYS).toString());
                    orderRepository.saveAndFlush(order);

                    //Extracting information about the products in the order to send them in the email
                    List<Map<String, Object>> products = userOrdersDao.getProductsForOrder(orderId);
                    StringBuilder productInformation = new StringBuilder();

                    //Adding product information(product toString) to the StringBuilder then sending the whole text in the email.
                    for (Map<String, Object> p : products) {
                        for (Map.Entry<String, Object> entry : p.entrySet()) {
                            productInformation.append(productRepository.getOne(Long.valueOf((Integer) entry.getValue())).toString());
                        }
                    }

                    new Thread(() -> {
                        try {
                            MailUtil.sendMail(serverEmailAddress, user.getEmail(),
                                    "Order confirmation",
                                    MailUtil.ORDER_CONFIRMATION_MESSAGE + order.toString() + productInformation);
                        } catch (MessagingException e) {
                            log.log(Priority.WARN, e.getMessage(), e);
                        }
                    }).start();

                    response.getWriter().append(OrderMessage.orderMessage(order, user.getEmail()));
                    return;
                }
            }
        }
        throw new NoOrdersAvailableException();
    }



}
