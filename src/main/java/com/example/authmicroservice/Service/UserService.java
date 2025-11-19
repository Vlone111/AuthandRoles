package com.example.authmicroservice.Service;


import com.example.authmicroservice.Dao.UserRepository;
import com.example.authmicroservice.Entity.Roles;
import com.example.authmicroservice.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User createUser(String email){
        User user = new User();
        user.setEmail(email);
        user.setRole(Roles.USER);
        userRepository.save(user);
        return user;
    }
}
