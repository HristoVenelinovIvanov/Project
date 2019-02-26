package com.example.demo.Model.DAO;

import com.example.demo.Model.POJO.User;
import com.example.demo.Model.Utility.Exceptions.InvalidCredentinalsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.Map;

@Component
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public boolean checkIfUserExists(String username, String password) {

        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[] { username, password }, Integer.class);

        return count > 0;
    }
}
