package com.techouts.service;

import com.techouts.entity.CartItem;
import com.techouts.entity.Order;
import com.techouts.entity.OrderItems;
import com.techouts.entity.Products;
import com.techouts.entity.User;
import com.techouts.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemsService cartItemsService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, CartItemsService cartItemsService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.cartItemsService = cartItemsService;
        this.productService = productService;
    }

    @Transactional
    public String placeOrderFromCart(User user, String shippingAddress, String paymentMethod) {
        if (shippingAddress == null || shippingAddress.isBlank()) {
            return "Address is required.";
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            return "Payment method is required.";
        }

        List<CartItem> cartItems = cartItemsService.getItemsByUserId(user.getId());
        if (cartItems.isEmpty()) {
            return "Your cart is empty.";
        }

        for (CartItem cartItem : cartItems) {
            if (cartItem.getQuantity() > cartItem.getProduct().getStock()) {
                return "Stock is insufficient for " + cartItem.getProduct().getName() + ".";
            }
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setShippingAddress(shippingAddress.trim());
        order.setPaymentMethod(paymentMethod.trim());

        List<OrderItems> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : cartItems) {
            Products product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.save(product);

            OrderItems item = new OrderItems();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice());
            orderItems.add(item);

            total += product.getPrice() * cartItem.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);
        orderRepository.save(order);

        for (CartItem cartItem : cartItems) {
            cartItemsService.removeItem(user.getId(), cartItem.getId());
        }

        return "Order placed successfully.";
    }

    @Transactional
    public String placeDirectOrder(User user,
                                   Products product,
                                   int quantity,
                                   String shippingAddress,
                                   String paymentMethod) {
        if (shippingAddress == null || shippingAddress.isBlank()) {
            return "Address is required.";
        }
        if (paymentMethod == null || paymentMethod.isBlank()) {
            return "Payment method is required.";
        }
        if (quantity < 1) {
            return "Quantity should be at least 1.";
        }
        if (product.getStock() < quantity) {
            return "Requested quantity exceeds stock.";
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");
        order.setShippingAddress(shippingAddress.trim());
        order.setPaymentMethod(paymentMethod.trim());

        OrderItems item = new OrderItems();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());

        List<OrderItems> orderItems = new ArrayList<>();
        orderItems.add(item);
        order.setOrderItems(orderItems);
        order.setTotalAmount(product.getPrice() * quantity);

        product.setStock(product.getStock() - quantity);
        productService.save(product);

        orderRepository.save(order);
        return "Order placed successfully.";
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
