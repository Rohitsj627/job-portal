package com.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    @Column(length = 2000)
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String salary;
    private String category;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    @NotNull(message = "Employer is required")
    private User employer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<Application> applications;

    public enum JobType {
        FULL_TIME, PART_TIME, CONTRACT, FREELANCE, INTERNSHIP
    }

    // Constructors
    public Job() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Job(String title, String description, String location, String salary, 
               String category, JobType jobType, User employer) {
        this();
        this.title = title;
        this.description = description;
        this.location = location;
        this.salary = salary;
        this.category = category;
        this.jobType = jobType;
        this.employer = employer;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}