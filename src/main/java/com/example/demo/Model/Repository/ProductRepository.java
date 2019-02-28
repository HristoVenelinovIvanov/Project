package com.example.demo.Model.Repository;

import com.example.demo.Model.POJO.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



}
