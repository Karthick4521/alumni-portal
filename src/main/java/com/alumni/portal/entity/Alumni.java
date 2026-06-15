package com.alumni.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alumni")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    private String degree;
    private String department;
    private String company;
    private String designation;
    private String location;
    private String phone;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}