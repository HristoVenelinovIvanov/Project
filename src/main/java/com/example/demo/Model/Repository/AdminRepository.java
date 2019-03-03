package com.example.demo.Model.Repository;

import com.example.demo.Model.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<User, Long> {
}
