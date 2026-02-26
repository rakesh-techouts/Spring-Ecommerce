package com.techouts.repositoryimpl;

import com.techouts.entity.Order;
import com.techouts.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryImplementation implements OrderRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Order save(Order order) {
        return em.merge(order);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return em.createQuery("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.id = :userId ORDER BY o.orderDate DESC", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
