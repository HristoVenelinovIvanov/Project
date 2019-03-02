package com.example.demo.Model.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class ProductCategoryDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //****RETURNS FALSE**** if the category exists.
    public boolean categoryExists(long categoryToCheck) {

        String sql = "SELECT COUNT(*) FROM categories WHERE category_id = ?";

        return !(jdbcTemplate.queryForObject(sql,new Object[] {categoryToCheck}, int.class) > 0);
    }
}
