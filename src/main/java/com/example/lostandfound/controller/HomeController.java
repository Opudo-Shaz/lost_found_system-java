package com.example.lostandfound.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home(Model model) {
        model.addAttribute("isRootUri", true); // Add the attribute for root URI
        return "index"; // Your view name
    }

}
