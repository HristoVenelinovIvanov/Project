package com.example.demo.Model.DAO;


import com.example.demo.Model.Utility.Exceptions.UserNotFoundExeption;
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

    public boolean checkIfUserExists(String username, String password) throws UserNotFoundExeption {

        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";

        int count = jdbcTemplate.queryForObject(sql, new Object[] { username, password }, Integer.class);
        if (count > 0) {
            throw new UserNotFoundExeption();
        }

        return true;
    }

}