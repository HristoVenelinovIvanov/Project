package com.example.demo.model.repository;

import com.example.demo.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String containsName);
    Product findByProductId(long productId);
    List<Product> findAllByCategoryId(long categoryId);


}
