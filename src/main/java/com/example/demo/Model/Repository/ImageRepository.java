package com.example.demo.Model.Repository;

import com.example.demo.Model.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<User, String> {

    User getImageByUserId(String image);
}
