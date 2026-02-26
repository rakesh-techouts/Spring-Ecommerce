package com.techouts.service;

import com.techouts.entity.Cart;
import com.techouts.entity.CartItem;
import com.techouts.entity.Products;
import com.techouts.entity.User;
import com.techouts.repository.CartItemsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemsService {

    private final CartItemsRepository cartItemsRepository;

    public CartItemsService(CartItemsRepository cartItemsRepository) {
        this.cartItemsRepository = cartItemsRepository;
    }

    @Transactional
    public String addToCart(User user, Products product, int quantity) {
        if (quantity < 1) {
            return "Quantity should be at least 1.";
        }
        if (product.getStock() <= 0) {
            return "Product is out of stock.";
        }
        if (quantity > product.getStock()) {
            return "Requested quantity is greater than available stock.";
        }

        Cart cart = cartItemsRepository.findOrCreateCart(user.getId());
        Optional<CartItem> existingItem = cartItemsRepository.findItemByCartAndProduct(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQty = item.getQuantity() + quantity;
            if (newQty > product.getStock()) {
                return "Total quantity in cart exceeds stock.";
            }
            item.setQuantity(newQty);
            cartItemsRepository.saveCartItem(item);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItemsRepository.saveCartItem(item);
        }
        return "Item added to cart.";
    }

    @Transactional(readOnly = true)
    public List<CartItem> getItemsByUserId(Long userId) {
        return cartItemsRepository.findItemsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public long getCartCount(Long userId) {
        return cartItemsRepository.countItemsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Integer getQuantityByItemId(Long userId, Long itemId) {
        return cartItemsRepository.findItemById(itemId)
                .filter(i -> i.getCart().getUser().getId().equals(userId))
                .map(CartItem::getQuantity)
                .orElse(null);
    }

    @Transactional
    public String updateQuantity(Long userId, Long itemId, int quantity) {
        Optional<CartItem> optionalItem = cartItemsRepository.findItemById(itemId);
        if (optionalItem.isEmpty()) {
            return "Cart item not found.";
        }

        CartItem item = optionalItem.get();
        if (!item.getCart().getUser().getId().equals(userId)) {
            return "Unauthorized cart action.";
        }

        if (quantity <= 0) {
            cartItemsRepository.deleteCartItem(item);
            return "Item removed from cart.";
        }

        if (quantity > item.getProduct().getStock()) {
            return "Quantity exceeds available stock.";
        }

        item.setQuantity(quantity);
        cartItemsRepository.saveCartItem(item);
        return "Cart updated.";
    }

    @Transactional
    public String removeItem(Long userId, Long itemId) {
        Optional<CartItem> optionalItem = cartItemsRepository.findItemById(itemId);
        if (optionalItem.isEmpty()) {
            return "Cart item not found.";
        }

        CartItem item = optionalItem.get();
        if (!item.getCart().getUser().getId().equals(userId)) {
            return "Unauthorized cart action.";
        }

        cartItemsRepository.deleteCartItem(item);
        return "Item removed from cart.";
    }

    @Transactional(readOnly = true)
    public double getCartTotal(Long userId) {
        return getItemsByUserId(userId).stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
    }
}
