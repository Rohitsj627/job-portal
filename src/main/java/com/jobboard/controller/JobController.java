package com.jobboard.controller;

import com.jobboard.entity.Application;
import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import com.jobboard.service.ApplicationService;
import com.jobboard.service.JobService;
import com.jobboard.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/jobs")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping("/post")
    public String showPostJobForm(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        if (user == null || user.getRole() != User.Role.EMPLOYER) {
            return "redirect:/";
        }
        
        model.addAttribute("job", new Job());
        return "jobs/post-job";
    }
    
    @PostMapping("/post")
    public String postJob(@Valid @ModelAttribute Job job, BindingResult result, 
                         Authentication authentication, Model model) {
        if (result.hasErrors()) {
            return "jobs/post-job";
        }
        
        User employer = userService.findByUsername(authentication.getName()).orElse(null);
        if (employer == null || employer.getRole() != User.Role.EMPLOYER) {
            return "redirect:/";
        }
        
        job.setEmployer(employer);
        jobService.createJob(job);
        return "redirect:/dashboard";
    }
    
    @GetMapping("/{id}")
    public String viewJob(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<Job> job = jobService.findById(id);
        if (job.isEmpty()) {
            return "redirect:/";
        }
        
        model.addAttribute("job", job.get());
        
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.findByUsername(authentication.getName()).orElse(null);
            model.addAttribute("currentUser", user);
            
            if (user != null && user.getRole() == User.Role.CANDIDATE) {
                boolean hasApplied = applicationService.hasUserApplied(user, job.get());
                model.addAttribute("hasApplied", hasApplied);
                model.addAttribute("application", new Application());
            }
            
            if (user != null && user.getRole() == User.Role.EMPLOYER && 
                job.get().getEmployer().getId().equals(user.getId())) {
                List<Application> applications = applicationService.getApplicationsByJob(job.get());
                model.addAttribute("applications", applications);
            }
        }
        
        return "jobs/view-job";
    }
    
    @PostMapping("/{id}/apply")
    public String applyForJob(@PathVariable Long id, @ModelAttribute Application application,
                             Authentication authentication) {
        User candidate = userService.findByUsername(authentication.getName()).orElse(null);
        if (candidate == null || candidate.getRole() != User.Role.CANDIDATE) {
            return "redirect:/login";
        }
        
        Optional<Job> job = jobService.findById(id);
        if (job.isEmpty()) {
            return "redirect:/";
        }
        
        if (!applicationService.hasUserApplied(candidate, job.get())) {
            application.setJob(job.get());
            application.setCandidate(candidate);
            applicationService.createApplication(application);
        }
        
        return "redirect:/jobs/" + id + "?applied=true";
    }
    
    @GetMapping("/{id}/edit")
    public String editJob(@PathVariable Long id, Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        Optional<Job> job = jobService.findById(id);
        
        if (job.isEmpty() || user == null || 
            !job.get().getEmployer().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("job", job.get());
        return "jobs/edit-job";
    }
    
    @PostMapping("/{id}/edit")
    public String updateJob(@PathVariable Long id, @Valid @ModelAttribute Job job, 
                           BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "jobs/edit-job";
        }
        
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        Optional<Job> existingJob = jobService.findById(id);
        
        if (existingJob.isEmpty() || user == null || 
            !existingJob.get().getEmployer().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }
        
        job.setId(id);
        job.setEmployer(user);
        job.setCreatedAt(existingJob.get().getCreatedAt());
        jobService.updateJob(job);
        
        return "redirect:/dashboard";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteJob(@PathVariable Long id, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        Optional<Job> job = jobService.findById(id);
        
        if (job.isPresent() && user != null && 
            job.get().getEmployer().getId().equals(user.getId())) {
            jobService.deleteJob(id);
        }
        
        return "redirect:/dashboard";
    }
}