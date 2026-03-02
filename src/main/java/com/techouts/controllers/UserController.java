package com.techouts.controllers;

import com.techouts.entity.User;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    //regex for 1-LowerCase/1-UpperCase/1-SpecialSymbol/1-Digit/length>=7
    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{7,}$");
    private final UserService userService;

    //constructor injection of dependencies
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "authRequired", required = false) String authRequired,
                        @RequestParam(value = "registered", required = false) String registered,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("success", "Logged out successfully.");
        }
        if (authRequired != null) {
            model.addAttribute("warning", "Please login to continue.");
        }
        if (registered != null) {
            model.addAttribute("success", "Registration successful. Please login.");
        }
        return "login";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }
    @PostMapping("register")
    public String registerSubmit(@RequestParam("name")  String name,
                                 @RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("password") String password,
                                 Model model) {
        //Checking for if any Null values exits
        if (name==null||username==null||username.isBlank()||email == null || email.isBlank() || phone == null || phone.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("warning", "All fields are required.");
            return "register";
        }

        //Normalizing email and username
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedPhone = phone.trim();
        String normalizedUsername = username.trim();
        
        //checking the format of Email as per email
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            model.addAttribute("warning", "Enter a valid email address.");
            return "register";
        }
        
        //checking phone format
        if (!PHONE_PATTERN.matcher(normalizedPhone).matches()) {
            model.addAttribute("warning", "Enter a valid 10-digit phone number starting with 6-9.");
            return "register";
        }
        
        if (!STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
            model.addAttribute("warning", "Password must be 8+ chars with upper, lower, digit, and special character.");
            return "register";
        }
        
        //checking for any user exists with the given username
        if (userService.usernameExists(normalizedUsername)) {
            model.addAttribute("error", "Username already registered. Please choose a different username.");
            return "register";
        }
        
        //checking for any user exists with the given mail
        if (userService.emailExists(normalizedEmail)) {
            model.addAttribute("error", "Email already registered. Please login instead.");
            return "register";
        }
        
        //checking for any user exists with the given phone
        if (userService.phoneExists(normalizedPhone)) {
            model.addAttribute("error", "Phone number already registered. Please login instead.");
            return "register";
        }
        
        //if all conditions fails then Create a New User and redirect to home page
        User user = new User();
        user.setName(name);
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPhone(normalizedPhone);
        user.setPassword(password);
        userService.register(user);
        return "redirect:/login?registered=true";
    }


    @GetMapping("profile")
    public String profile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }
        
        Optional<User> user = userService.findById(userId);
        if (user.isEmpty()) {
            return "redirect:/login?authRequired=true";
        }
        
        model.addAttribute("user", user.get());
        return "profile";
    }
    
    @PostMapping("profile")
    public String updateProfile(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("phone") String phone,
                               @RequestParam("address") String address,
                               HttpSession session,
                               Model model) {
        Long userId = (Long) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login?authRequired=true";
        }
        
        Optional<User> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            return "redirect:/login?authRequired=true";
        }
        
        //Validation
        if (username == null || username.isBlank() || email == null || email.isBlank() || phone == null || phone.isBlank()) {
            model.addAttribute("warning", "Username, email and phone are required.");
            model.addAttribute("user", userOpt.get());
            return "profile";
        }
        
        String normalizedUsername = username.trim();
        String normalizedEmail = email.trim().toLowerCase();
        String normalizedPhone = phone.trim();
        
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            model.addAttribute("warning", "Enter a valid email address.");
            model.addAttribute("user", userOpt.get());
            return "profile";
        }
        
        if (!PHONE_PATTERN.matcher(normalizedPhone).matches()) {
            model.addAttribute("warning", "Enter a valid 10-digit phone number starting with 6-9.");
            model.addAttribute("user", userOpt.get());
            return "profile";
        }
        
        User user = userOpt.get();
        
        //Check if username exists for other users
        if (!normalizedUsername.equals(user.getUsername()) && userService.usernameExistsForOtherUsers(userId, normalizedUsername)) {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("user", user);
            return "profile";
        }
        
        //Check if email exists for other users
        if (!normalizedEmail.equals(user.getEmail()) && userService.emailExistsForOtherUsers(userId, normalizedEmail)) {
            model.addAttribute("error", "Email already exists.");
            model.addAttribute("user", user);
            return "profile";
        }
        
        //Check if phone exists for other users
        if (!normalizedPhone.equals(user.getPhone()) && userService.phoneExistsForOtherUsers(userId, normalizedPhone)) {
            model.addAttribute("error", "Phone number already exists.");
            model.addAttribute("user", user);
            return "profile";
        }
        
        //Update user
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPhone(normalizedPhone);
        user.setAddress(address != null ? address.trim() : "");
        
        userService.updateProfile(user);
        
        //Update session name
        session.setAttribute("USER_NAME", user.getName());
        
        model.addAttribute("success", "Profile updated successfully!");
        model.addAttribute("user", user);
        return "profile";
    }

    @RequestMapping("/auth-required")
    public String authRequired() {
        return "redirect:/login?authRequired=true";
    }
}
