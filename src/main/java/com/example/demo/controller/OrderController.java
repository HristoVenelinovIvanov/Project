package com.example.demo.controller;

import com.example.demo.model.dao.OrderedProductsDao;
import com.example.demo.model.dao.ProductDao;
import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import com.example.demo.model.pojo.User;
import com.example.demo.model.repository.OrderRepository;
import com.example.demo.model.repository.ProductRepository;
import com.example.demo.utility.exceptions.ProductExceptions.ProductDoesNotExistException;
import com.example.demo.utility.mail.MailUtil;
import com.example.demo.utility.ordermessages.OrderMessage;
import com.github.lambdaexpression.annotation.RequestBodyParam;
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

    public static final int DELETE_COMMA = 1;
    public static final int DELETE_WHERE_CLAUSE = 5;

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
                        //TODO Deal with message not sending
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

        System.out.println(discounted);
        StringBuffer select = new StringBuffer("SELECT price ,product_name ,");
        StringBuffer from = new StringBuffer("FROM test1339.products ");
        StringBuffer where = new StringBuffer("WHERE");

        if (productName != null && !productName.isEmpty()) {
            where.append(" product_name =").append("'").append(productName).append("' AND ");
        }
        if (price != null && !price.isEmpty()) {
            where.append(" price =").append(price).append(" AND ");
        }
        if (discounted != null && !discounted.isEmpty()) {
            select.append("discounted ,");
            where.append(" discounted =").append("'").append(discounted).append("' AND ");
        }
        if (brand != null && !brand.isEmpty()) {
            select.append("brand ,");
            where.append(" brand =").append("'").append(brand).append("' AND ");
        }
        if (inches != null && !inches.isEmpty()) {
            select.append("inches ,");
            where.append(" inches =").append(inches).append(" AND ");
        }
        if (frequency != null && !frequency.isEmpty()) {
            select.append("frequency ,");
            where.append(" frequency =").append(frequency).append(" AND ");
        }
        if (kw != null && !kw.isEmpty()) {
            select.append("kw ,");
            where.append(" kw =").append(kw).append(" AND ");
        }
        if (numberOfHobs != null && !numberOfHobs.isEmpty()) {
            select.append("number_of_hobs ,");
            where.append(" number_of_hobs =").append(numberOfHobs).append(" AND ");
        }
        if (timer != null && !timer.isEmpty()) {
            select.append("timer ,");
            where.append(" timer =").append(timer).append(" AND ");
        }
        if (cameraPixels != null && !cameraPixels.isEmpty()) {
            select.append("camera_pixels ,");
            where.append(" camera_pixels =").append(cameraPixels).append(" IS NOT NULL AND ");
        }
        if (fingerPrint != null && !fingerPrint.isEmpty()) {
            select.append("finger_print ,");
            where.append(" finger_print =").append(fingerPrint).append(" AND ");
        }
        if (waterProof != null && !waterProof.isEmpty()) {
            select.append("water_proof ,");
            where.append(" water_proof =").append(waterProof).append(" AND ");
        }
        if (kgCapacity != null && !kgCapacity.isEmpty()) {
            select.append("kg_capacity ,");
            where.append(" kg_capacity =").append(kgCapacity).append(" AND ");
        }
        if (intelligentWash != null && !intelligentWash.isEmpty()) {
            select.append("intelligent_wash ,");
            where.append(" intelligent_wash =").append(intelligentWash).append(" AND ");
        }
        if (inbuildDryer != null && !inbuildDryer.isEmpty()) {
            select.append("inbuild_dryer ,");
            where.append(" inbuild_dryer =").append(inbuildDryer).append(" AND ");
        }

        StringBuffer sql = new StringBuffer();
        select.setLength(select.length() - DELETE_COMMA);
        where.setLength(where.length() - DELETE_WHERE_CLAUSE);
        sql.append(select).append(from).append(where);
        System.out.println(sql);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
        System.out.println(list.toString());
        return jdbcTemplate.queryForList(sql.toString());

    }



}
