package com.example.authmicroservice.Controller;


import com.example.authmicroservice.Dao.UserRepository;
import com.example.authmicroservice.Entity.User;
import com.example.authmicroservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole(USER)")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/getALL")
    public ResponseEntity<?> existbyemail(@RequestBody String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getall() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
