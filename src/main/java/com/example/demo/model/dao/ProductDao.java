package com.example.demo.model.dao;

import com.example.demo.model.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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


    public ResultSet getAllFilteredProducts(long inches) throws Exception{

        try {
            Connection con = jdbcTemplate.getDataSource().getConnection();
            {
                String sql = "SELECT * FROM products WHERE inches = ?";
                List<Product> productsList = new ArrayList<>();
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setLong(1, inches);
                ResultSet rs = ps.executeQuery();

                return rs;
            }
        }
        catch (Exception e){


        }
        return null;
    }

    public long decreaseQuantity(long quantity, long productId) {

        String sql = "UPDATE products SET quantity -= ? WHERE product_id = ?";

        return jdbcTemplate.update(sql, new Object[] {quantity, productId}, long.class);
    }

}
