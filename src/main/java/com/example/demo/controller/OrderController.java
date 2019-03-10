package com.example.demo.controller;

import com.example.demo.model.dao.OrderedProductsDao;
import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.model.repository.UserRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.mail.MailUtil;
import com.example.demo.utility.ordermessages.OrderMessage;
import com.github.lambdaexpression.annotation.RequestBodyParam;
import org.apache.log4j.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;

    //Ordering only one product
    @RequestMapping(value = "/products/{productId}/order", method = RequestMethod.POST)
    @Transactional
    public void orderProduct(@PathVariable("productId") long productId, @RequestBody Order order, HttpSession session, HttpServletResponse response) throws Exception {
        validateLogin(session);

        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            User user = (User) session.getAttribute("userLogged");

            if (productDao.checkQuantity(productId) >= order.getQuantity()) {
                productDao.orderDecreaseQuantity(order.getQuantity(), productOptional.get());
                order.setUserId(user.getUserId());
                orderRepository.save(order);
                orderedProductsDao.addToOrdered(order.getOrderId(), user.getUserId());

                new Thread(() -> {
                    try {
                        MailUtil.sendMail(serverEmailAddress, user.getEmail(),
                                "Order confirmation",
                                MailUtil.ORDER_CONFIRMATION_MESSAGE + order.toString() + productOptional.get().toString());
                    } catch (MessagingException e) {
                        log.log(Priority.WARN, e.getMessage(), e);
                    }
                }).start();

                response.getWriter().append(OrderMessage.orderMessage(order, user.getEmail()));
            } else {
                response.getWriter().append("We don't have enough in stock of product with ID " + productId);
            }
        } else {
            throw new ProductDoesNotExistException("The product you are trying to buy does not exists!");
        }
    }

    @RequestMapping(value = "products/filter/", method = RequestMethod.GET)
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
            @RequestParam(value = "inbuild_dryer", required = false) String inbuildDryer) {

        String sql = productDao.filterProducts(productName, price, discounted, brand, inches, frequency, kw, numberOfHobs,timer,
                cameraPixels, fingerPrint, waterProof, kgCapacity, intelligentWash, inbuildDryer);
        return jdbcTemplate.queryForList(sql);
    }


}
