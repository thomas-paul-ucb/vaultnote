package com.vaultnote.vaultnote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/auth/signup")
    public String signup(@RequestBody User user) {
        userRepository.save(user);
        return "User created";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody User loginRequest) {
        Optional<User> foundUser = userRepository.findByUsername(loginRequest.getUsername());

        if (foundUser.isEmpty()) {
            return "User not found";
        }

        if (!foundUser.get().getPassword().equals(loginRequest.getPassword())) {
            return "Wrong password";
        }

        return "Login successful";
    }
}