package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Company is required")
    @Column(nullable = false)
    private String company;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "last_date")
    private LocalDate lastDate;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}