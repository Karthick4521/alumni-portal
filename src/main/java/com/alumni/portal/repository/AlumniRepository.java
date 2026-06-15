package com.alumni.portal.repository;

import com.alumni.portal.entity.Alumni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlumniRepository extends JpaRepository<Alumni, Long> {

    // Find by department
    List<Alumni> findByDepartment(String department);

    // Find by graduation year
    List<Alumni> findByGraduationYear(Integer year);

    // Find by company
    List<Alumni> findByCompanyContainingIgnoreCase(String company);

    // Find by user id
    Optional<Alumni> findByUserId(Long userId);

    // Search by name, company, department, location
    @Query("SELECT a FROM Alumni a WHERE " +
            "LOWER(a.fullName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(a.company) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(a.department) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(a.location) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(a.designation) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Alumni> searchAlumni(@Param("keyword") String keyword);

    // Filter by department and year
    @Query("SELECT a FROM Alumni a WHERE " +
            "(:dept IS NULL OR a.department = :dept) AND " +
            "(:year IS NULL OR a.graduationYear = :year)")
    List<Alumni> filterAlumni(
            @Param("dept") String department,
            @Param("year") Integer year);
}