package com.example.demo.Model.Repository;

import com.example.demo.Model.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    User findByUserId(long userId);

}
