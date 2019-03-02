package com.example.demo.Model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserFavoritesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addToFavorites(long userId, long productId) {

        String sql = "INSERT INTO user_favorites (user_id, product_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, userId, productId);
        return true;
    }

}
