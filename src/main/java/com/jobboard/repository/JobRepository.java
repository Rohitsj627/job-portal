package com.jobboard.repository;

import com.jobboard.entity.Job;
import com.jobboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);
    
    @Query("SELECT j FROM Job j WHERE " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:category IS NULL OR j.category = :category)")
    List<Job> findJobsWithFilters(@Param("title") String title, 
                                  @Param("location") String location, 
                                  @Param("category") String category);
    
    List<Job> findByOrderByCreatedAtDesc();
    
    @Query("SELECT DISTINCT j.category FROM Job j WHERE j.category IS NOT NULL")
    List<String> findDistinctCategories();
}