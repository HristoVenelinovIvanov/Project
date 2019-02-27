package com.example.demo.Model.DAO;

import com.example.demo.Model.POJO.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String addProduct(Product product){
        String sql = "INSERT INTO products (product_name, price, quantity, category_id, a_category_id, characteristics) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, product.getProductName(), product.getPrice(), product.getQuantity(), product.getCategoryId(), product.getACategoryId(), product.getCharacteristics());
        return "Product added to category: " + product.getCategoryId() + " with ID: " + jdbcTemplate.queryForObject(sql, (resultSet, i) -> product.getProductId());
    }


}
