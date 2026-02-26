package com.techouts.repository;

import com.techouts.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRespository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String identity);
}
