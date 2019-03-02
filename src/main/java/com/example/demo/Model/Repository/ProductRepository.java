package com.example.demo.Model.Repository;

import com.example.demo.Model.POJO.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String containsName);


}
