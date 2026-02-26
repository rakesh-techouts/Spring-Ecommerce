package com.techouts.repository;

import com.techouts.entity.Products;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Products> findAll();
    List<Products> findByCategory(String category);
    Optional<Products> findById(Long id);
    Products save(Products product);
}
