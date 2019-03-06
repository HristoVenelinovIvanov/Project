package com.example.demo.model.repository;

import com.example.demo.model.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByCategoryName(String name);
}
