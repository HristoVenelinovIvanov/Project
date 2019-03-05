package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean productExists(long productId) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, long.class) > 0;
    }

    public boolean productExists(String productName) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_name LIKE LOWER(?)";

        return jdbcTemplate.queryForObject(sql, new Object[] {"%" +productName + "%"}, long.class) > 0;
    }

    public int checkQuantity(long productId) {

        String sql = "SELECT quantity FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, int.class);
    }

}
