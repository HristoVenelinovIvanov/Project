package com.example.demo.Model.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String findHashedPassword(String email) {

        String sql = "SELECT password FROM users WHERE email = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {email}, String.class);
    }

    public long getUserId(String email) {

        String sql = "SELECT user_id FROM users WHERE email = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{email}, long.class);
    }


}