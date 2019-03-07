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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

    //TODO not finished
    @RequestMapping(value = "products/filter/", method = RequestMethod.GET)
    public List<Product> filter(@RequestParam(value = "product_name", required = false) String productName,
                                @RequestParam(value = "price", required = false) double price,
                                @RequestParam(value = "discounted", required = false) double discounted,
                                @RequestParam(value = "brand", required = false) String brand,
                                @RequestParam(value = "inches", required = false) double inches,
                                @RequestParam(value = "frequency", required = false) long frequency,
                                @RequestParam(value = "kw", required = false) long kw,
                                @RequestParam(value = "number_of_hobs", required = false) long numberOfHobs,
                                @RequestParam(value = "timer", required = false) long timer,
                                @RequestParam(value = "camera_pixels", required = false) double cameraPixels,
                                @RequestParam(value = "finger_print", required = false) long fingerPrint,
                                @RequestParam(value = "water_proof", required = false) long waterProof,
                                @RequestParam(value = "kg_capacity", required = false) long kgCapacity,
                                @RequestParam(value = "intelligent_wash", required = false) long intelligentWash,
                                @RequestParam(value = "inbuild_dryer", required = false) long inbuildDryer,
                                HttpSession session, HttpServletRequest request) throws Exception {

        List<Product> list = new ArrayList<>();
        Long accIdLong = null;

        /*
        @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public List<Product> getAllWhere(@RequestParam(value = "acc", required = false) String accId,
                                                  @RequestParam(value = "cat", required = false) String catId,
                                                  @RequestParam(value = "from", required = false) String startDate,
                                                  @RequestParam(value = "to", required = false) String endDate,
                                                  @RequestParam(value = "income", required = false) String income,
                                                  @RequestParam(value = "order", required = false) String order,
                                                  @RequestParam(value = "desc", required = false) String desc,
                                                  HttpSession session, HttpServletRequest request) throws NotLoggedInException, UnauthorizedAccessException, IOException, InvalidRequestDataException, ForbiddenRequestException, SQLException, NotFoundException {
        User u = getLoggedValidUserFromSession(session, request);
        Long accIdLong = null;
        if (accId != null) {
            accIdLong = parseNumber(accId);
            ReturnAccountDTO a = accountController.getAccByIdLong(accIdLong,session,request);
        }
        Category c = null;
        Long catIdLong = null;
        if (catId != null) {
            catIdLong = parseNumber(catId);
            c = categoryController.getCategoryById(catIdLong,session,request);
        }
        Long startDateMillis = (startDate != null)? parseNumber(startDate): 0L;
        Long endDateMillis = (endDate != null)? parseNumber(endDate): System.currentTimeMillis();

        Boolean isIncome = null;
        if (income != null) {
            if (income.equalsIgnoreCase("true")) isIncome = true;
            if (income.equalsIgnoreCase("false")) isIncome = false;
            if (c!=null && c.isIncome() != isIncome){
                isIncome = c.isIncome();
            }
        }

        AbstractDao.SQLColumnName columnName = AbstractDao.SQLColumnName.EXECUTION_DATE;
        if (order != null) {
            switch (order) {
                case "amount": {
                    columnName = AbstractDao.SQLColumnName.AMOUNT;
                    break;
                }
                case "tname": {
                    columnName = AbstractDao.SQLColumnName.TRANSACTION_NAME;
                    break;
                }
                case "aname": {
                    columnName = AbstractDao.SQLColumnName.ACCOUNT_NAME;
                    break;
                }
            }
        }

         */



        return list;
    }
}
