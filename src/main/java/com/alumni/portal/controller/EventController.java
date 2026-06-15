package com.alumni.portal.controller;

import com.alumni.portal.entity.Event;
import com.alumni.portal.entity.User;
import com.alumni.portal.service.EventService;
import com.alumni.portal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    // LIST ALL EVENTS
    @GetMapping("")
    public String listEvents(
            @RequestParam(required = false) String keyword,
            Model model,
            Authentication authentication) {

        List<Event> eventList = eventService.searchEvents(keyword);
        model.addAttribute("eventList", eventList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", eventList.size());

        // Check registrations for logged-in user
        if (authentication != null) {
            String username = authentication.getName();
            Optional<User> user = userService.findByUsername(username);
            user.ifPresent(u -> {
                eventList.forEach(event -> {
                    model.addAttribute("registered_" + event.getId(),
                            eventService.isUserRegistered(
                                    event.getId(), u.getId()));
                });
            });
        }
        return "events/list";
    }

    // SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/add";
    }

    // SAVE EVENT
    @PostMapping("/add")
    public String saveEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "events/add";
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        user.ifPresent(event::setCreatedBy);

        eventService.saveEvent(event);
        redirectAttributes.addFlashAttribute("successMsg",
                "Event created successfully!");
        return "redirect:/events";
    }

    // VIEW EVENT
    @GetMapping("/view/{id}")
    public String viewEvent(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        Optional<Event> event = eventService.getEventById(id);
        if (event.isEmpty()) {
            return "redirect:/events";
        }

        model.addAttribute("event", event.get());
        model.addAttribute("registrationCount",
                eventService.getRegistrationCount(id));
        model.addAttribute("registrations",
                eventService.getEventRegistrations(id));

        if (authentication != null) {
            String username = authentication.getName();
            Optional<User> user = userService.findByUsername(username);
            user.ifPresent(u -> model.addAttribute("isRegistered",
                    eventService.isUserRegistered(id, u.getId())));
        }
        return "events/view";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Event> event = eventService.getEventById(id);
        if (event.isEmpty()) {
            return "redirect:/events";
        }
        model.addAttribute("event", event.get());
        return "events/edit";
    }

    // UPDATE EVENT
    @PostMapping("/edit/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "events/edit";
        }

        Optional<Event> existing = eventService.getEventById(id);
        if (existing.isPresent()) {
            event.setId(id);
            event.setCreatedBy(existing.get().getCreatedBy());
            event.setCreatedAt(existing.get().getCreatedAt());
            eventService.saveEvent(event);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Event updated successfully!");
        }
        return "redirect:/events";
    }

    // DELETE EVENT
    @GetMapping("/delete/{id}")
    public String deleteEvent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Event deleted successfully!");
        return "redirect:/events";
    }

    // REGISTER FOR EVENT
    @PostMapping("/register/{id}")
    public String registerForEvent(
            @PathVariable Long id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Optional<Event> event = eventService.getEventById(id);
        if (event.isEmpty()) {
            return "redirect:/events";
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            boolean success = eventService.registerForEvent(
                    event.get(), user.get());
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg",
                        "Successfully registered for event!");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "You are already registered!");
            }
        }
        return "redirect:/events/view/" + id;
    }
}