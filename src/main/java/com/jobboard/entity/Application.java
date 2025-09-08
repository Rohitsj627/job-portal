package com.jobboard.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private User candidate;

    @Column(length = 1000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime appliedAt;

    public enum ApplicationStatus {
        PENDING, REVIEWED, ACCEPTED, REJECTED
    }

    // Constructors
    public Application() {
        this.appliedAt = LocalDateTime.now();
        this.status = ApplicationStatus.PENDING;
    }

    public Application(Job job, User candidate, String coverLetter) {
        this();
        this.job = job;
        this.candidate = candidate;
        this.coverLetter = coverLetter;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public User getCandidate() { return candidate; }
    public void setCandidate(User candidate) { this.candidate = candidate; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }
}