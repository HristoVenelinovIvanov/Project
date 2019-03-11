package com.example.demo.model.repository;

import com.example.demo.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ImageRepository extends JpaRepository<User, String> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "update product_images set product_id = ? where image_id = ?", nativeQuery = true)
    void addImageToProduct(long productId, long imageId);

}
