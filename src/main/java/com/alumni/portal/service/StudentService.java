package com.alumni.portal.service;

import com.alumni.portal.entity.Student;
import com.alumni.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUserId(Long userId) {
        return studentRepository.findByUserId(userId);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return studentRepository.findAll();
        }
        return studentRepository.searchStudents(keyword.trim());
    }

    public long countStudents() {
        return studentRepository.count();
    }
}