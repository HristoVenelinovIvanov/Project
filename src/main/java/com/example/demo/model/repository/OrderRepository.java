package com.example.demo.model.repository;

import com.example.demo.model.pojo.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderDetails, Long> {



}
