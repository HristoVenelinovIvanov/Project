package com.example.demo.model.repository;

import com.example.demo.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findByUserId(long userId);

}
