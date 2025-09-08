package com.jobboard.controller;

import com.jobboard.entity.User;
import com.jobboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "auth/register";
        }
        
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            return "auth/register";
        }
        
        userService.createUser(user);
        return "redirect:/login?registered=true";
    }
}