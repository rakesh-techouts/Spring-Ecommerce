package com.techouts.controllers;

import com.techouts.entity.User;
import com.techouts.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    //regex for 1-LowerCase/1-UpperCase/1-SpecialSymbol/1-Digit/length>=7
    private static final Pattern STRONG_PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{7,}$");
    private final UserService userService;

    //constructor injection of dependencies
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }
    //Checking for user Existence
    @PostMapping("login")
    public String loginSubmit(@RequestParam("identity") String identity,
                              @RequestParam("password") String password,
                              HttpServletRequest request,
                              HttpSession session,
                              Model model) {
        if (identity == null || identity.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("warning", "Please enter username/email and password.");
            return "login";
        }

        Optional<User> user = userService.authenticate(identity.trim(), password);

        if (user.isEmpty()) {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "login";
        }
        // invalidate the current session and created a fresh Session for user
        session.invalidate();
        HttpSession freshSession = request.getSession(true);
        freshSession.setAttribute("USER_ID", user.get().getId());
        freshSession.setAttribute("USER_NAME", user.get().getUsername());
        return "redirect:/home";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();   //destroying the existence session and redirect to the index page
        return "redirect:/";
    }

    @GetMapping("register")
    public String register() {
        return "register";
    }
    @PostMapping("register")
    public String registerSubmit(@RequestParam("username") String username,
                                 @RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request,
                                 HttpSession session,
                                 Model model) {
        //Checking for if any Null values exits
        if (username == null || username.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            model.addAttribute("warning", "All fields are required.");
            return "register";
        }

        //Normalizing email
        String normalizedEmail = email.trim().toLowerCase();
        //checking the format of Email as per email
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            model.addAttribute("warning", "Enter a valid email address.");
            return "register";
        }
        if (!STRONG_PASSWORD_PATTERN.matcher(password).matches()) {
            model.addAttribute("warning", "Password must be 8+ chars with upper, lower, and digit.");
            return "register";
        }
        //checking for any user exists with the given mail
        if (userService.emailExists(normalizedEmail)) {
            model.addAttribute("error", "Email already registered. Please login instead.");
            return "register";
        }
        //if all conditions fails then Create a New User and redirect to home page
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(password);
        User savedUser = userService.register(user);

        session.invalidate();
        HttpSession freshSession = request.getSession(true);
        freshSession.setAttribute("USER_ID", savedUser.getId());// adding user-id to session
        freshSession.setAttribute("USER_NAME", savedUser.getUsername()); // adding user-name to the session
        return "redirect:/home";
    }


    @RequestMapping("/auth-required")
    public String authRequired() {
        return "redirect:/login?authRequired=true";
    }
}
