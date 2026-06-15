package com.alumni.portal.controller;

import com.alumni.portal.entity.Job;
import com.alumni.portal.entity.User;
import com.alumni.portal.service.JobService;
import com.alumni.portal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    // LIST ALL JOBS
    @GetMapping("")
    public String listJobs(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Job> jobList = jobService.searchJobs(keyword);
        model.addAttribute("jobList", jobList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", jobList.size());
        return "jobs/list";
    }

    // SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("job", new Job());
        return "jobs/add";
    }

    // SAVE JOB
    @PostMapping("/add")
    public String saveJob(
            @Valid @ModelAttribute("job") Job job,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "jobs/add";
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        user.ifPresent(job::setPostedBy);

        jobService.saveJob(job);
        redirectAttributes.addFlashAttribute("successMsg",
                "Job posted successfully!");
        return "redirect:/jobs";
    }

    // VIEW JOB
    @GetMapping("/view/{id}")
    public String viewJob(
            @PathVariable Long id,
            Model model,
            Authentication authentication) {

        Optional<Job> job = jobService.getJobById(id);
        if (job.isEmpty()) {
            return "redirect:/jobs";
        }

        model.addAttribute("job", job.get());
        model.addAttribute("applicationCount",
                jobService.getApplicationCount(id));
        model.addAttribute("applications",
                jobService.getJobApplications(id));

        if (authentication != null) {
            String username = authentication.getName();
            Optional<User> user = userService.findByUsername(username);
            user.ifPresent(u -> model.addAttribute("hasApplied",
                    jobService.hasApplied(id, u.getId())));
        }
        return "jobs/view";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Job> job = jobService.getJobById(id);
        if (job.isEmpty()) {
            return "redirect:/jobs";
        }
        model.addAttribute("job", job.get());
        return "jobs/edit";
    }

    // UPDATE JOB
    @PostMapping("/edit/{id}")
    public String updateJob(
            @PathVariable Long id,
            @Valid @ModelAttribute("job") Job job,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "jobs/edit";
        }

        Optional<Job> existing = jobService.getJobById(id);
        if (existing.isPresent()) {
            job.setId(id);
            job.setPostedBy(existing.get().getPostedBy());
            job.setCreatedAt(existing.get().getCreatedAt());
            jobService.saveJob(job);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Job updated successfully!");
        }
        return "redirect:/jobs";
    }

    // DELETE JOB
    @GetMapping("/delete/{id}")
    public String deleteJob(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        jobService.deleteJob(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Job deleted successfully!");
        return "redirect:/jobs";
    }

    // APPLY FOR JOB
    @PostMapping("/apply/{id}")
    public String applyForJob(
            @PathVariable Long id,
            @RequestParam(required = false) String coverLetter,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Optional<Job> job = jobService.getJobById(id);
        if (job.isEmpty()) {
            return "redirect:/jobs";
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            boolean success = jobService.applyForJob(
                    job.get(), user.get(), coverLetter);
            if (success) {
                redirectAttributes.addFlashAttribute("successMsg",
                        "Application submitted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "You have already applied for this job!");
            }
        }
        return "redirect:/jobs/view/" + id;
    }
}