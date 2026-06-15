package com.alumni.portal.repository;

import com.alumni.portal.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Find upcoming events
    List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(
            LocalDate date);

    // Find past events
    List<Event> findByEventDateBeforeOrderByEventDateDesc(
            LocalDate date);

    // Search events
    @Query("SELECT e FROM Event e WHERE " +
            "LOWER(e.title) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(e.location) LIKE LOWER(CONCAT('%',:keyword,'%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    List<Event> searchEvents(@Param("keyword") String keyword);
}