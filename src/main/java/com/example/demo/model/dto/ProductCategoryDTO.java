package com.example.demo.model.dto;

import com.example.demo.model.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Component
public class ProductCategoryDTO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CategoryRepository categoryRepository;


    //****RETURNS FALSE**** if the category exists.
    public boolean categoryExists(long categoryToCheck) {

        String sql = "SELECT COUNT(*) FROM categories WHERE category_id = ?";

        return !(jdbcTemplate.queryForObject(sql,new Object[] {categoryToCheck}, int.class) > 0);
    }

    //****RETURNS FALSE**** if the category exists.
    public boolean categoryExists(String categoryName) {

        String sql = "SELECT COUNT(*) FROM categories WHERE category_name = ?";

        return !(jdbcTemplate.queryForObject(sql,new Object[] {categoryName}, int.class) > 0);
    }




}
