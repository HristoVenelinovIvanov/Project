package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class UserOrdersDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addToUserOrders(long orderId, long userId) {

        String sql = "INSERT INTO user_orders (order_id, user_id) VALUES (?, ?)";

        return jdbcTemplate.update(sql, orderId, userId) > 0;
    }

    public List<Map<String, Object>> getProductsForOrder(long orderId) {

        String sql = "SELECT product_id from ordered_products WHERE order_id = ?";

        return jdbcTemplate.queryForList(sql, new Object[] {orderId});
    }


}