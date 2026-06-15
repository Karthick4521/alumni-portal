package com.alumni.portal.service;

import com.alumni.portal.entity.Alumni;
import com.alumni.portal.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlumniService {

    @Autowired
    private AlumniRepository alumniRepository;

    // Get all alumni
    public List<Alumni> getAllAlumni() {
        return alumniRepository.findAll();
    }

    // Get alumni by ID
    public Optional<Alumni> getAlumniById(Long id) {
        return alumniRepository.findById(id);
    }

    // Get alumni by user ID
    public Optional<Alumni> getAlumniByUserId(Long userId) {
        return alumniRepository.findByUserId(userId);
    }

    // Save alumni
    public Alumni saveAlumni(Alumni alumni) {
        return alumniRepository.save(alumni);
    }

    // Update alumni
    public Alumni updateAlumni(Alumni alumni) {
        return alumniRepository.save(alumni);
    }

    // Delete alumni
    public void deleteAlumni(Long id) {
        alumniRepository.deleteById(id);
    }

    // Search alumni
    public List<Alumni> searchAlumni(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return alumniRepository.findAll();
        }
        return alumniRepository.searchAlumni(keyword.trim());
    }

    // Filter alumni
    public List<Alumni> filterAlumni(String department, Integer year) {
        return alumniRepository.filterAlumni(department, year);
    }

    // Count total alumni
    public long countAlumni() {
        return alumniRepository.count();
    }
}