package com.techouts.service;

import com.techouts.entity.Cart;
import com.techouts.entity.User;
import com.techouts.repository.UserRespository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRespository userRespository, PasswordEncoder passwordEncoder) {
        this.userRespository = userRespository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        return userRespository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRespository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean phoneExists(String phone) {
        return userRespository.existsByPhone(phone);
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userRespository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> authenticate(String identity, String password) {
        // Try username first
        Optional<User> user = userRespository.findByUsername(identity)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
        
        if (user.isPresent()) {
            return user;
        }
        
        // Try email
        user = userRespository.findByEmail(identity)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
        
        if (user.isPresent()) {
            return user;
        }
        
        // Try phone
        return userRespository.findByPhone(identity)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRespository.findById(id);
    }

    @Transactional
    public User updateProfile(User user) {
        return userRespository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean emailExistsForOtherUsers(Long userId, String email) {
        return userRespository.findByEmail(email)
                .map(user -> !user.getId().equals(userId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean phoneExistsForOtherUsers(Long userId, String phone) {
        return userRespository.findByPhone(phone)
                .map(user -> !user.getId().equals(userId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean usernameExistsForOtherUsers(Long userId, String username) {
        return userRespository.findByUsername(username)
                .map(user -> !user.getId().equals(userId))
                .orElse(false);
    }
}
