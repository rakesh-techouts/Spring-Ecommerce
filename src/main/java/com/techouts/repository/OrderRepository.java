package com.techouts.repository;

import com.techouts.entity.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);
    List<Order> findByUserId(Long userId);
}
