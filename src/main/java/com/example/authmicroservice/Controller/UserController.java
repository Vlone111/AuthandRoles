package com.example.authmicroservice.Controller;


import com.example.authmicroservice.Dao.UserRepository;
import com.example.authmicroservice.Dto.SurnameAndNameRequest;
import com.example.authmicroservice.Entity.User;
import com.example.authmicroservice.Jwt.UserDetailsImpl;
import com.example.authmicroservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole(USER)")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/setDetails")
    public ResponseEntity<?> setUserSurnameAndName(@RequestBody SurnameAndNameRequest surnameAndNameRequest) {
        String name = surnameAndNameRequest.getName();
        String surname = surnameAndNameRequest.getSurname();
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        userService.setSurnameAndName(name, surname, principal);
        return ResponseEntity.ok().build();
    }

}
