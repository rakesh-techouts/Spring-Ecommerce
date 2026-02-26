package com.techouts.repositoryimpl;

import com.techouts.entity.Cart;
import com.techouts.entity.CartItem;
import com.techouts.entity.User;
import com.techouts.repository.CartItemsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemsRepositoryImplementation implements CartItemsRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cart findOrCreateCart(Long userId) {
        List<Cart> carts = em.createQuery("SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();

        if (!carts.isEmpty()) {
            return carts.get(0);
        }

        User user = em.find(User.class, userId);
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        em.persist(cart);
        return cart;
    }

    @Override
    public Cart saveCart(Cart cart) {
        return em.merge(cart);
    }

    @Override
    public CartItem saveCartItem(CartItem cartItem) {
        return em.merge(cartItem);
    }

    @Override
    public List<CartItem> findItemsByUserId(Long userId) {
        return em.createQuery("SELECT ci FROM CartItem ci JOIN FETCH ci.product p JOIN ci.cart c WHERE c.user.id = :userId ORDER BY ci.id DESC", CartItem.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public Optional<CartItem> findItemById(Long itemId) {
        return Optional.ofNullable(em.find(CartItem.class, itemId));
    }

    @Override
    public Optional<CartItem> findItemByCartAndProduct(Long cartId, Long productId) {
        List<CartItem> items = em.createQuery("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId", CartItem.class)
                .setParameter("cartId", cartId)
                .setParameter("productId", productId)
                .setMaxResults(1)
                .getResultList();
        return items.stream().findFirst();
    }

    @Override
    public long countItemsByUserId(Long userId) {
        Long count = em.createQuery("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.cart.user.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count == null ? 0 : count;
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        CartItem managed = em.contains(cartItem) ? cartItem : em.merge(cartItem);
        em.remove(managed);
    }
}
