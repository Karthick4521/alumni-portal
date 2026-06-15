package com.alumni.portal.controller;

import com.alumni.portal.service.AlumniService;
import com.alumni.portal.service.EventService;
import com.alumni.portal.service.JobService;
import com.alumni.portal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EventService eventService;

    @Autowired
    private JobService jobService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("alumniCount",
                alumniService.countAlumni());
        model.addAttribute("studentCount",
                studentService.countStudents());
        model.addAttribute("eventCount",
                eventService.countEvents());
        model.addAttribute("jobCount",
                jobService.countJobs());
        return "dashboard";
    }
}