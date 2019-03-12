package com.example.demo.model.repository;

import com.example.demo.model.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query(value = "SELECT image_name FROM product_images WHERE product_id = ?", nativeQuery = true)
    String getImageNameByProductId(long productId);

}
