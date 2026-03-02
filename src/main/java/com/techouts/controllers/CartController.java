package com.techouts.controllers;

import com.techouts.entity.User;
import com.techouts.service.CartItemsService;
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

@Controller
public class CartController {

    private final CartItemsService cartItemsService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartItemsService cartItemsService,
                          ProductService productService,
                          UserService userService) {
        this.cartItemsService = cartItemsService;
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            @RequestParam(value = "redirectTo", defaultValue = "home") String redirectTo,
                            @RequestParam(value = "redirectProductId", required = false) Long redirectProductId,
                            HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login?authRequired=true";
        }

        return productService.findById(productId)
                .map(product -> {
                    String message = cartItemsService.addToCart(user, product, quantity);
                    if (message.toLowerCase().contains("added")) {
                        return "redirect:" + appendQuery(basePath(redirectTo, redirectProductId), "message", message);
                    }
                    return "redirect:" + appendQuery(basePath(redirectTo, redirectProductId), "warning", message);
                })
                .orElse("redirect:" + appendQuery(basePath(redirectTo, redirectProductId), "warning", "Product not found."));
    }

    @GetMapping("/cart")
    public String cart(@RequestParam(value = "message", required = false) String message,
                       @RequestParam(value = "warning", required = false) String warning,
                       HttpSession session,
                       Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        model.addAttribute("items", cartItemsService.getItemsByUserId(userId));
        model.addAttribute("cartCount", cartItemsService.getCartCount(userId));
        model.addAttribute("total", cartItemsService.getCartTotal(userId));
        model.addAttribute("username", session.getAttribute("USER_NAME"));

        if (message != null && !message.isBlank()) {
            model.addAttribute("success", message);
        }
        if (warning != null && !warning.isBlank()) {
            model.addAttribute("warning", warning);
        }

        return "cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("itemId") Long itemId,
                             @RequestParam("quantity") int quantity,
                             HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        String message = cartItemsService.updateQuantity(userId, itemId, quantity);
        if (message.toLowerCase().contains("updated") || message.toLowerCase().contains("removed")) {
            return "redirect:/cart?message=" + encode(message);
        }
        return "redirect:/cart?warning=" + encode(message);
    }

    @PostMapping("/cart/increase")
    public String increase(@RequestParam("itemId") Long itemId,
                           @RequestParam(value = "redirectTo", defaultValue = "cart") String redirectTo,
                           HttpSession session) {
        return changeByDelta(itemId, 1, redirectTo, session);
    }

    @PostMapping("/cart/decrease")
    public String decrease(@RequestParam("itemId") Long itemId,
                           @RequestParam(value = "redirectTo", defaultValue = "cart") String redirectTo,
                           HttpSession session) {
        return changeByDelta(itemId, -1, redirectTo, session);
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("itemId") Long itemId, HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        String message = cartItemsService.removeItem(userId, itemId);
        if (message.toLowerCase().contains("removed")) {
            return "redirect:/cart?message=" + encode(message);
        }
        return "redirect:/cart?warning=" + encode(message);
    }

    private String changeByDelta(Long itemId, int delta, String redirectTo, HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        Integer current = cartItemsService.getQuantityByItemId(userId, itemId);
        if (current == null) {
            return "redirect:" + appendQuery(basePath(redirectTo, null), "warning", "Cart item not found.");
        }

        String message = cartItemsService.updateQuantity(userId, itemId, current + delta);
        if (message.toLowerCase().contains("updated") || message.toLowerCase().contains("removed")) {
            return "redirect:" + appendQuery(basePath(redirectTo, null), "message", message);
        }
        return "redirect:" + appendQuery(basePath(redirectTo, null), "warning", message);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String basePath(String redirectTo, Long redirectProductId) {
        if ("index".equalsIgnoreCase(redirectTo)) {
            return "/";
        }
        if ("cart".equalsIgnoreCase(redirectTo)) {
            return "/cart";
        }
        if ("product".equalsIgnoreCase(redirectTo) && redirectProductId != null) {
            return "/product?id=" + redirectProductId;
        }
        return "/home";
    }

    private String appendQuery(String path, String key, String value) {
        String delimiter = path.contains("?") ? "&" : "?";
        return path + delimiter + key + "=" + encode(value);
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        String message = cartItemsService.clearCart(userId);
        if (message.toLowerCase().contains("cleared") || message.toLowerCase().contains("emptied")) {
            return "redirect:/home?message=" + encode(message);
        }
        return "redirect:/home?warning=" + encode(message);
    }
}
