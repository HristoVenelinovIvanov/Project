package com.example.demo.utility.ordermessages;

import com.example.demo.model.pojo.OrderDetails;
import com.example.demo.model.pojo.User;
import com.example.demo.model.pojo.UserAddress;

import java.time.LocalDate;


public abstract class OrderMessage {


    public static String orderMessage(OrderDetails orderDetails, User user, UserAddress userAddress) {

        return "Your order number is #" + orderDetails.getOrderId()
                + "\nYou'll receive an email confirmation shorty to "
                + user.getEmail() + " Date ordered: ("
                + LocalDate.now().toString()
                + ")\nShipping Address\n" + userAddress.toString();
    }
}
