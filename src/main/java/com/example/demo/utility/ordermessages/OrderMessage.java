package com.example.demo.utility.ordermessages;

import com.example.demo.model.pojo.Order;
import com.example.demo.model.pojo.Product;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class OrderMessage {

    public static String orderMessage(Order order, String email) {

        return "Your order number is #" + order.getOrderId()
                + "\nYou'll receive an email confirmation shortly to "
                + email + " \nDate ordered: ("
                + LocalDateTime.now().toString()
                + ")\nDate expected : (" + order.getRequiredDate()
                + ")\nShipping Address:\n" + order.getAddress();
    }

    public static String productMessage(Product product, Order order) {
        return "\n\nProduct name = '" + product.getProductName() + "\nPrice = " + order.getQuantity() * product.getPrice() + "лв.";
    }

    public static String notConfirmedOrderMessage(Order order, String email) {

        return "Your order number is #" + order.getOrderId()
                + "\nE-mail: " + email
                + "\nStatus: Not confirmed"
                + " \nDate : (" + LocalDateTime.now().toString()
                + ")\nDate expected if ordered until midnight: (" + LocalDate.now().plus(4, ChronoUnit.DAYS).toString() + ")";
    }

}
