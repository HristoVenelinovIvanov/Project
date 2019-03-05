package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserFavoritesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean addToFavorites(long userId, long productId) {

        String sql = "INSERT INTO user_favorites (user_id, product_id) VALUES (?, ?)";

        return jdbcTemplate.update(sql, userId, productId) > 0;
    }

    public boolean removeFromFavorites(long userId, long productId) {

        String sql = "DELETE FROM user_favorites WHERE user_id = ? AND product_id = ?";

        return jdbcTemplate.update(sql, userId, productId) > 0;
    }

    public boolean isFavorite(long userId, long productId) {

        String sql = "SELECT COUNT(*) FROM user_favorites WHERE user_id = ? AND product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {userId, productId}, int.class) > 0;
    }

}
