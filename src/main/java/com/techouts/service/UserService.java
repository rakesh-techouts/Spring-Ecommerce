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
    public Optional<User> authenticate(String identity, String password) {
        return userRespository.findByUsernameOrEmail(identity)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRespository.findById(id);
    }
}
