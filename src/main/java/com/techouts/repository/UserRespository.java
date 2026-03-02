package com.techouts.repository;

import com.techouts.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRespository {
    User save(User user);
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsername(String username);
    Optional<User> findByEmailOrPhone(String identifier);
}
