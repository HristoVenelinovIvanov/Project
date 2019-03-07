package com.example.demo.utility.ordermessages;

import com.example.demo.model.pojo.Order;
import java.time.LocalDate;

public abstract class OrderMessage {

    public static String orderMessage(Order order, String email) {

        return "Your order number is #" + order.getOrderId()
                + "\nYou'll receive an email confirmation shortly to "
                + email + " \nDate ordered: ("
                + LocalDate.now().toString()
                + ")\nDate expected : (" + order.getRequiredDate()
                + ")\nShipping Address\n" + order.getAddress();
    }
}
