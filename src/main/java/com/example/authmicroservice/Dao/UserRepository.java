package com.example.authmicroservice.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.authmicroservice.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
