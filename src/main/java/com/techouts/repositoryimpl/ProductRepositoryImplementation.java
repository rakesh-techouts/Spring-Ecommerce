package com.techouts.repositoryimpl;

import com.techouts.entity.Products;
import com.techouts.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImplementation implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Products> findAll() {
        return em.createQuery("SELECT p FROM Products p ORDER BY p.id DESC", Products.class).getResultList();
    }

    @Override
    public List<Products> findByCategory(String category) {
        return em.createQuery("SELECT p FROM Products p WHERE LOWER(p.category) = LOWER(:category) ORDER BY p.id DESC", Products.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public Optional<Products> findById(Long id) {
        return Optional.ofNullable(em.find(Products.class, id));
    }

    @Override
    public Products save(Products product) {
        return em.merge(product);
    }
}
