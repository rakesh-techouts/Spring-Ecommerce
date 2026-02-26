package com.techouts.controllers;

import com.techouts.entity.CartItem;
import com.techouts.entity.Products;
import com.techouts.service.CartItemsService;
import com.techouts.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private static final String CAT_ALL = "All";

    private final ProductService productService;
    private final CartItemsService cartItemsService;

    //Constructor Injection
    public ProductController(ProductService productService, CartItemsService cartItemsService) {
        this.productService = productService;
        this.cartItemsService = cartItemsService;
    }

    @GetMapping({"/", "/index"})
    public String index(@RequestParam(value = "category", required = false) String category,
                        @RequestParam(value = "message", required = false) String message,
                        @RequestParam(value = "warning", required = false) String warning,
                        HttpSession session,
                        Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        addCategoryProducts(model, category);

        if (userId != null) {
            List<CartItem> cartItems = cartItemsService.getItemsByUserId(userId);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("username", session.getAttribute("USER_NAME"));
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartCount", cartItemsService.getCartCount(userId));
            model.addAttribute("cartTotal", cartItemsService.getCartTotal(userId));
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        if (message != null && !message.isBlank()) {
            model.addAttribute("success", message);
        }
        if (warning != null && !warning.isBlank()) {
            model.addAttribute("warning", warning);
        }

        return "index";
    }

    @GetMapping("/home")
    public String home(@RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "message", required = false) String message,
                       @RequestParam(value = "warning", required = false) String warning,
                       HttpSession session,
                       Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }

        addCategoryProducts(model, category);
        model.addAttribute("username", session.getAttribute("USER_NAME"));
        model.addAttribute("cartCount", cartItemsService.getCartCount(userId));
        model.addAttribute("cartItems", cartItemsService.getItemsByUserId(userId));

        if (message != null && !message.isBlank()) {
            model.addAttribute("success", message);
        }
        if (warning != null && !warning.isBlank()) {
            model.addAttribute("warning", warning);
        }

        return "home";
    }

    @GetMapping("/product")
    public String productDetails(@RequestParam("id") Long id,
                                 @RequestParam(value = "message", required = false) String message,
                                 @RequestParam(value = "warning", required = false) String warning,
                                 HttpSession session,
                                 Model model) {
        Products product = productService.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/?warning=Product%20not%20found.";
        }

        model.addAttribute("product", product);
        Long userId = (Long) session.getAttribute("USER_ID");
        boolean loggedIn = userId != null;
        model.addAttribute("isLoggedIn", loggedIn);

        if (loggedIn) {
            model.addAttribute("username", session.getAttribute("USER_NAME"));
            model.addAttribute("cartCount", cartItemsService.getCartCount(userId));
        }

        if (message != null && !message.isBlank()) {
            model.addAttribute("success", message);
        }
        if (warning != null && !warning.isBlank()) {
            model.addAttribute("warning", warning);
        }
        return "product-details";
    }

    private void addCategoryProducts(Model model, String category) {
        String selectedCategory = (category == null || category.isBlank()) ? CAT_ALL : category;
        model.addAttribute("category", selectedCategory);
        model.addAttribute("products", productService.getProducts(selectedCategory));
        model.addAttribute("laptops", productService.getProducts("Laptop"));
        model.addAttribute("mobiles", productService.getProducts("Mobile"));
        model.addAttribute("buds", productService.getProducts("Buds"));
    }
}
