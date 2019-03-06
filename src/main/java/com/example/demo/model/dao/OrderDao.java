package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDao  {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean addToOrder(long orderId, long productId){

        String sql = "INSERT INTO order_products (order_id, product_id) VALUES (?, ?)";

        return jdbcTemplate.update(sql, orderId, productId) > 0;
    }

}
