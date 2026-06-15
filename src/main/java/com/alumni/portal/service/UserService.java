package com.alumni.portal.service;

import com.alumni.portal.entity.User;
import com.alumni.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    public User registerUser(User user) {
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Check if username already exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email already exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}