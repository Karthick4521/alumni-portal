package com.alumni.portal.repository;

import com.alumni.portal.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByJobId(Long jobId);

    List<JobApplication> findByUserId(Long userId);

    Optional<JobApplication> findByJobIdAndUserId(
            Long jobId, Long userId);

    boolean existsByJobIdAndUserId(Long jobId, Long userId);

    long countByJobId(Long jobId);
}