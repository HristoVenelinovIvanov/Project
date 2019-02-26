package com.example.demo.Model.DAO;

import com.example.demo.Model.Utility.Exceptions.InvalidCredentinalsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String userLogin() throws InvalidCredentinalsException {

      //  throw new InvalidCredentinalsException("Invalid email or password entered!");

        return "You have successfully logged in";
    }
}
