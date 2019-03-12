package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderedProductsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addToOrdered(long order_id, long productId, long quantity) {

        String sql = "INSERT INTO ordered_products (order_id, product_id, quantity) VALUES (?, ?, ?)";

        return jdbcTemplate.update(sql, order_id, productId, quantity) > 0;
    }
}