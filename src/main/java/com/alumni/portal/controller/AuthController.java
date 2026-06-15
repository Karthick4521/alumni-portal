package com.alumni.portal.controller;

import com.alumni.portal.entity.User;
import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.EventService;
import com.alumni.portal.service.JobService;
import com.alumni.portal.service.StudentService;
import com.alumni.portal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private JobService jobService;

    // ===== LOGIN =====
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("errorMsg",
                    "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg",
                    "Logged out successfully.");
        }
        return "login";
    }

    // ===== REGISTER GET =====
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ===== REGISTER POST =====
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("usernameError",
                    "Username already taken!");
            return "register";
        }
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError",
                    "Email already registered!");
            return "register";
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login?registered=true";
    }

    // ===== ADMIN DASHBOARD =====
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("alumniCount",
                alumniService.countAlumni());
        model.addAttribute("studentCount",
                studentService.countStudents());
        model.addAttribute("eventCount",
                eventService.countEvents());
        model.addAttribute("jobCount",
                jobService.countJobs());
        return "admin/dashboard";
    }

    // ===== ALUMNI DASHBOARD =====
    @GetMapping("/alumni/dashboard")
    public String alumniDashboard(Model model) {
        model.addAttribute("pageTitle", "Alumni Dashboard");
        return "alumni/dashboard";
    }

    // ===== STUDENT DASHBOARD =====
    @GetMapping("/student/dashboard")
    public String studentDashboard(Model model) {
        model.addAttribute("pageTitle", "Student Dashboard");
        return "student/dashboard";
    }

    // ===== CREATE ADMIN (TEMPORARY) =====
    @GetMapping("/create-admin")
    public String createAdmin(Model model) {
        if (!userService.usernameExists("superadmin")) {
            User admin = new User();
            admin.setUsername("superadmin");
            admin.setEmail("superadmin@portal.com");
            admin.setPassword("admin123");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userService.registerUser(admin);
        }
        return "redirect:/login?registered=true";
    }
}