package com.example.demo.model.repository;

import com.example.demo.model.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User findByUserId(long userId);

    @Query(value = "SELECT * FROM users where subscribed = 1", nativeQuery = true)
    List<User> findAllBySubscribed();

    @Query(value = "SELECT * from users where user_role = 1", nativeQuery = true)
    List<User> findAllByUserRoleAdministrator();

    @Query(value = "SELECT user_id from users WHERE email = ?", nativeQuery = true)
    Long findUserIdByEmail(String email);

}

