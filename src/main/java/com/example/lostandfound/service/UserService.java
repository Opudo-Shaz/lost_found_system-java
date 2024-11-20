package com.example.lostandfound.service;

import com.example.lostandfound.entity.User;
import com.example.lostandfound.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        // Encrypt the user's password using BCryptPasswordEncoder
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // Save the user to the repository
        userRepository.save(user);
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public void logUserAvatarImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            System.out.println("Avatar Image: " + principal.getAvatarImage());  // Log the avatar image
        }
    }

    // Method to retrieve email by username
    public String getEmailByUsername(String username) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getEmail();  // Return the user's email
        } else {

            throw new IllegalArgumentException("User not found with username: " + username);
        }
    }

    public String getUserEmailByUsername(String finderUsername) {
        return userRepository.findByUsername(finderUsername).getEmail();
    }
}
