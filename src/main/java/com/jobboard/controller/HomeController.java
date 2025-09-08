package com.jobboard.controller;

import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import com.jobboard.service.JobService;
import com.jobboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home(Model model, Authentication authentication,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) String category) {
        
        List<Job> jobs;
        if (title != null || location != null || category != null) {
            jobs = jobService.searchJobs(title, location, category);
        } else {
            jobs = jobService.getAllJobs();
        }
        
        model.addAttribute("jobs", jobs);
        model.addAttribute("categories", jobService.getAllCategories());
        model.addAttribute("searchTitle", title);
        model.addAttribute("searchLocation", location);
        model.addAttribute("searchCategory", category);
        
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            model.addAttribute("currentUser", user);
        }
        
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
}