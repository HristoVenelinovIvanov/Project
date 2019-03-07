package com.example.demo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
public class ProductCategoryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        public List<Map<String, Object>> allPhones() {

        String sql = "select p.* from products as p join categories as c on p.category_id=c.category_id where c.parent_id = 4";

        return jdbcTemplate.queryForList(sql);
    }


}
