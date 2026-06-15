package com.alumni.portal.service;

import com.alumni.portal.entity.Job;
import com.alumni.portal.entity.JobApplication;
import com.alumni.portal.entity.User;
import com.alumni.portal.repository.JobApplicationRepository;
import com.alumni.portal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return jobRepository.findAll();
        }
        return jobRepository.searchJobs(keyword.trim());
    }

    public boolean applyForJob(Job job, User user, String coverLetter) {
        if (applicationRepository.existsByJobIdAndUserId(
                job.getId(), user.getId())) {
            return false;
        }
        JobApplication app = new JobApplication();
        app.setJob(job);
        app.setUser(user);
        app.setCoverLetter(coverLetter);
        app.setStatus(JobApplication.Status.PENDING);
        applicationRepository.save(app);
        return true;
    }

    public boolean hasApplied(Long jobId, Long userId) {
        return applicationRepository
                .existsByJobIdAndUserId(jobId, userId);
    }

    public long getApplicationCount(Long jobId) {
        return applicationRepository.countByJobId(jobId);
    }

    public List<JobApplication> getJobApplications(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<JobApplication> getUserApplications(Long userId) {
        return applicationRepository.findByUserId(userId);
    }

    public long countJobs() {
        return jobRepository.count();
    }
}