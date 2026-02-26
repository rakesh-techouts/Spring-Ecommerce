package com.techouts.repository;

import com.techouts.entity.Cart;
import com.techouts.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository {
    Cart findOrCreateCart(Long userId);
    Cart saveCart(Cart cart);
    CartItem saveCartItem(CartItem cartItem);
    List<CartItem> findItemsByUserId(Long userId);
    Optional<CartItem> findItemById(Long itemId);
    Optional<CartItem> findItemByCartAndProduct(Long cartId, Long productId);
    long countItemsByUserId(Long userId);
    void deleteCartItem(CartItem cartItem);
}
