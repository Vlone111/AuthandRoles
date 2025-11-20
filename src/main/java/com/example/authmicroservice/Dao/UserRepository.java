package com.example.authmicroservice.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.authmicroservice.Entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
