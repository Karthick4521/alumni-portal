package com.alumni.portal.service;

import com.alumni.portal.entity.Event;
import com.alumni.portal.entity.EventRegistration;
import com.alumni.portal.entity.User;
import com.alumni.portal.repository.EventRegistrationRepository;
import com.alumni.portal.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventRegistrationRepository registrationRepository;

    // Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Get event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Get upcoming events
    public List<Event> getUpcomingEvents() {
        return eventRepository
                .findByEventDateGreaterThanEqualOrderByEventDateAsc(
                        LocalDate.now());
    }

    // Save event
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Delete event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    // Search events
    public List<Event> searchEvents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return eventRepository.findAll();
        }
        return eventRepository.searchEvents(keyword.trim());
    }

    // Register user for event
    public boolean registerForEvent(Event event, User user) {
        if (registrationRepository.existsByEventIdAndUserId(
                event.getId(), user.getId())) {
            return false; // Already registered
        }
        EventRegistration reg = new EventRegistration();
        reg.setEvent(event);
        reg.setUser(user);
        registrationRepository.save(reg);
        return true;
    }

    // Check if user is registered
    public boolean isUserRegistered(Long eventId, Long userId) {
        return registrationRepository
                .existsByEventIdAndUserId(eventId, userId);
    }

    // Get registration count
    public long getRegistrationCount(Long eventId) {
        return registrationRepository.countByEventId(eventId);
    }

    // Get all registrations for event
    public List<EventRegistration> getEventRegistrations(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }

    // Count total events
    public long countEvents() {
        return eventRepository.count();
    }
}