package com.alumni.portal.repository;

import com.alumni.portal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByUserId(Long userId);

    List<Student> findByDepartment(String department);

    Optional<Student> findByEnrollmentNo(String enrollmentNo);

    @Query("SELECT s FROM Student s WHERE " +
            "LOWER(s.fullName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(s.department) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(s.enrollmentNo) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Student> searchStudents(@Param("keyword") String keyword);
}