package com.example.demo.model.repository;

import com.example.demo.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String containsName);

    Product findByProductId(long productId);

    List<Product> findAllByCategoryId(long categoryId);

    @Query(value = "SELECT * from products WHERE discounted > 0", nativeQuery = true)
    List<Product> findAllByDiscounted();
}
