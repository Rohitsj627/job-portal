package com.jobboard.service;

import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import com.jobboard.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }
    
    public List<Job> getAllJobs() {
        return jobRepository.findByOrderByCreatedAtDesc();
    }
    
    public List<Job> getJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }
    
    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }
    
    public List<Job> searchJobs(String title, String location, String category) {
        return jobRepository.findJobsWithFilters(title, location, category);
    }
    
    public List<String> getAllCategories() {
        return jobRepository.findDistinctCategories();
    }
    
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
    
    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }
}