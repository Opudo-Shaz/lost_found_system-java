package com.example.lostandfound.controller;

import com.example.lostandfound.entity.User;
import com.example.lostandfound.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This should match the name of your Thymeleaf template (login.html)
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        // Find the user by username
        User user = userService.findByUsername(username);

        // Check if user exists and password matches (assuming you have a method to validate password)
        if (user != null && user.getPassword().equals(password)) {
            // Authentication successful
            return "redirect:/dashboard"; // Redirect to the dashboard
        } else {
            // Authentication failed
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Return to the login page with error
        }
    }

        @GetMapping("/memberinfo")
        public String viewMemberInfo (Model model){
            model.addAttribute("members", userService.getAllUsers());
            return "memberinfo"; // Thymeleaf template
        }

        // Other user-related endpoints can be added here

        @PostMapping("/reset-password")
        public String resetPassword (@RequestParam String email, Model model){
            model.addAttribute("message", "Password reset link has been sent to your email.");
            return "login";
        }

    }

