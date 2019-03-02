package com.example.demo.Model.DAO;

import com.example.demo.Model.POJO.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public String addProduct(Product product){
        String sql = "INSERT INTO products (product_name, price, quantity, category_id, a_category_id, characteristics) VALUES (?, ?, ?, ?, ?, ?)";
        return "Product added to category: " + product.getCategoryId() + " with ID: " + jdbcTemplate.queryForObject(sql, (resultSet, i) -> product.getProductId());
    }

    public boolean productExists(long productId) {

        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[] {productId}, long.class) > 0;
    }

}
