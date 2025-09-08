package com.jobboard.controller;

import com.jobboard.entity.Application;
import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import com.jobboard.service.ApplicationService;
import com.jobboard.service.JobService;
import com.jobboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        if (user.getRole() == User.Role.EMPLOYER) {
            List<Job> jobs = jobService.getJobsByEmployer(user);
            model.addAttribute("jobs", jobs);
            return "dashboard/employer-dashboard";
        } else {
            List<Application> applications = applicationService.getApplicationsByCandidate(user);
            model.addAttribute("applications", applications);
            return "dashboard/candidate-dashboard";
        }
    }
    
    @PostMapping("/applications/update-status")
    public String updateApplicationStatus(@RequestParam Long applicationId, 
                                        @RequestParam Application.ApplicationStatus status,
                                        Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            return "redirect:/dashboard";
        }
        
        Optional<Application> application = applicationService.findById(applicationId);
        if (application.isPresent() && 
            application.get().getJob().getEmployer().getId().equals(user.getId())) {
            applicationService.updateApplicationStatus(applicationId, status);
        }
        
        return "redirect:/dashboard";
    }
}