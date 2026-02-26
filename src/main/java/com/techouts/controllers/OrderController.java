package com.techouts.controllers;

import com.techouts.entity.CartItem;
import com.techouts.entity.Products;
import com.techouts.entity.User;
import com.techouts.service.CartItemsService;
import com.techouts.service.OrderService;
import com.techouts.service.ProductService;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final CartItemsService cartItemsService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderService orderService;

    public OrderController(CartItemsService cartItemsService,
                          ProductService productService,
                          UserService userService,
                          OrderService orderService) {
        this.cartItemsService = cartItemsService;
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping("/buy-now")
    public String buyNow(@RequestParam("productId") Long productId,
                         @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                         HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        if (quantity < 1) {
            return "redirect:/home?warning=" + encode("Quantity should be at least 1.");
        }

        return "redirect:/checkout?productId=" + productId + "&quantity=" + quantity;
    }

    @GetMapping("/checkout")
    public String checkoutSummary(@RequestParam(value = "productId", required = false) Long productId,
                                  @RequestParam(value = "quantity", required = false) Integer quantity,
                                  @RequestParam(value = "warning", required = false) String warning,
                                  HttpSession session,
                                  Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        model.addAttribute("username", session.getAttribute("USER_NAME"));
        if (productId != null) {
            int directQty = (quantity == null || quantity < 1) ? 1 : quantity;
            Products product = productService.findById(productId).orElse(null);
            if (product == null) {
                return "redirect:/home?warning=" + encode("Product not found.");
            }

            CartItem preview = new CartItem();
            preview.setProduct(product);
            preview.setQuantity(directQty);

            List<CartItem> items = new ArrayList<>();
            items.add(preview);
            model.addAttribute("items", items);
            model.addAttribute("total", product.getPrice() * directQty);
            model.addAttribute("checkoutMode", "DIRECT");
            model.addAttribute("directProductId", productId);
            model.addAttribute("directQuantity", directQty);
        } else {
            model.addAttribute("items", cartItemsService.getItemsByUserId(userId));
            model.addAttribute("total", cartItemsService.getCartTotal(userId));
            model.addAttribute("checkoutMode", "CART");
        }

        if (warning != null && !warning.isBlank()) {
            model.addAttribute("warning", warning);
        }

        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(@RequestParam("shippingAddress") String shippingAddress,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(value = "checkoutMode", defaultValue = "CART") String checkoutMode,
                             @RequestParam(value = "productId", required = false) Long productId,
                             @RequestParam(value = "quantity", required = false) Integer quantity,
                             HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login?authRequired=true";
        }

        String message;
        if ("DIRECT".equalsIgnoreCase(checkoutMode) && productId != null) {
            Products product = productService.findById(productId).orElse(null);
            if (product == null) {
                return "redirect:/home?warning=" + encode("Product not found.");
            }
            int directQty = (quantity == null || quantity < 1) ? 1 : quantity;
            message = orderService.placeDirectOrder(user, product, directQty, shippingAddress, paymentMethod);
            if (!message.toLowerCase().contains("success")) {
                return "redirect:/checkout?productId=" + productId + "&quantity=" + directQty + "&warning=" + encode(message);
            }
        } else {
            message = orderService.placeOrderFromCart(user, shippingAddress, paymentMethod);
            if (!message.toLowerCase().contains("success")) {
                return "redirect:/checkout?warning=" + encode(message);
            }
        }

        if (message.toLowerCase().contains("success")) {
            return "redirect:/orders?message=" + encode(message);
        }
        return "redirect:/checkout?warning=" + encode(message);
    }

    @GetMapping("/orders")
    public String myOrders(@RequestParam(value = "message", required = false) String message,
                           HttpSession session,
                           Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        model.addAttribute("username", session.getAttribute("USER_NAME"));
        model.addAttribute("orders", orderService.getOrdersByUserId(userId));

        if (message != null && !message.isBlank()) {
            model.addAttribute("success", message);
        }

        return "orders";
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
