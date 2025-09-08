package com.jobboard.service;

import com.jobboard.entity.Application;
import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import com.jobboard.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    public Application createApplication(Application application) {
        return applicationRepository.save(application);
    }
    
    public List<Application> getApplicationsByCandidate(User candidate) {
        return applicationRepository.findByCandidate(candidate);
    }
    
    public List<Application> getApplicationsByJob(Job job) {
        return applicationRepository.findByJob(job);
    }
    
    public boolean hasUserApplied(User candidate, Job job) {
        return applicationRepository.existsByCandidateAndJob(candidate, job);
    }
    
    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }
    
    public Application updateApplicationStatus(Long id, Application.ApplicationStatus status) {
        Optional<Application> application = applicationRepository.findById(id);
        if (application.isPresent()) {
            Application app = application.get();
            app.setStatus(status);
            return applicationRepository.save(app);
        }
        return null;
    }
}