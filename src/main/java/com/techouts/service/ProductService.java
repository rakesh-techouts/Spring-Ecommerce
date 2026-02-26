package com.techouts.service;

import com.techouts.entity.Products;
import com.techouts.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Products> getProducts(String category) {
        if (category == null || category.isBlank() || "All".equalsIgnoreCase(category)) {
            return productRepository.findAll();
        }
        return productRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public Optional<Products> findById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Products save(Products product) {
        return productRepository.save(product);
    }
}
