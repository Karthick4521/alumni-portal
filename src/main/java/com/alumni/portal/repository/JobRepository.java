package com.alumni.portal.repository;

import com.alumni.portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByPostedById(Long userId);

    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Job> searchJobs(@Param("keyword") String keyword);
}