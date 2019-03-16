package com.example.demo.model.dao;

import com.example.demo.model.pojo.Product;
import com.example.demo.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ProductRepository productRepository;

    public static final int DELETE_COMMA = 1;
    public static final int DELETE_WHERE_CLAUSE = 5;
    volatile boolean flag = false;

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


    public void orderDecreaseQuantity(long quantity, Product product) {

            product.setQuantity(product.getQuantity() - quantity);
            product.setQuantityOnOrder(product.getQuantityOnOrder() + quantity);
            productRepository.saveAndFlush(product);

    }

    public List<Map<String, Object>> filterProducts(Map<String, String> list, Map<String, String> lowerHigher){

        StringBuffer select = new StringBuffer("SELECT * ");
        StringBuffer from = new StringBuffer("FROM test1339.products ");
        StringBuffer where = new StringBuffer();

        for (Iterator<Map.Entry<String, String>> it = list.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> next = it.next();
            appendSqlSelect(next, where);
        }

        for (Iterator<Map.Entry<String, String>> it = lowerHigher.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, String> next = it.next();
            appendSqlSelectLowerHigher(next, where);
        }

        flag = false;
        StringBuffer sql = new StringBuffer();
        sql.append(select).append(from).append(where);
        System.out.println(sql);
        return jdbcTemplate.queryForList(sql.toString());
    }

    private void appendSqlSelect(Map.Entry<String, String> next, StringBuffer where){
        if (next.getValue() != null && !next.getValue().isEmpty()){
            appendWhere(where);
            where.append(next.getKey() + " LIKE " + "'" + next.getValue() + "'");
        }
    }

    private void appendSqlSelectLowerHigher(Map.Entry<String, String> next, StringBuffer where){
        if (next.getValue() != null && !next.getValue().isEmpty()){
            appendWhere(where);
            if (next.getKey().equals("lower")) {
                where.append("price <" + "'" + next.getValue() + "'");
            }
            if (next.getKey().equals("higher")) {
                where.append("price >" + "'" + next.getValue() + "'");
            }
        }
    }
    private void appendWhere(StringBuffer where){
        if (flag){
            where.append(" AND ");
            return;
        }
        where.append("WHERE ");
        this.flag = true;
    }
}
