package com.alumni.portal.controller;

import com.alumni.portal.entity.Alumni;
import com.alumni.portal.entity.User;
import com.alumni.portal.service.AlumniService;
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
@RequestMapping("/alumni")
public class AlumniController {

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private UserService userService;

    // LIST ALL ALUMNI
    @GetMapping("")
    public String listAlumni(Model model) {
        List<Alumni> alumniList = alumniService.getAllAlumni();
        model.addAttribute("alumniList", alumniList);
        model.addAttribute("totalCount", alumniList.size());
        return "alumni/list";
    }

    // SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("alumni", new Alumni());
        return "alumni/add";
    }

    // SAVE NEW ALUMNI
    @PostMapping("/add")
    public String saveAlumni(
            @Valid @ModelAttribute("alumni") Alumni alumni,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            return "alumni/add";
        }
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        user.ifPresent(alumni::setUser);
        alumniService.saveAlumni(alumni);
        redirectAttributes.addFlashAttribute("successMsg",
                "Alumni profile created successfully!");
        return "redirect:/alumni";
    }

    // VIEW ALUMNI PROFILE
    @GetMapping("/view/{id}")
    public String viewAlumni(@PathVariable Long id, Model model) {
        Optional<Alumni> alumni = alumniService.getAlumniById(id);
        if (alumni.isEmpty()) {
            return "redirect:/alumni";
        }
        model.addAttribute("alumni", alumni.get());
        return "alumni/view";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Alumni> alumni = alumniService.getAlumniById(id);
        if (alumni.isEmpty()) {
            return "redirect:/alumni";
        }
        model.addAttribute("alumni", alumni.get());
        return "alumni/edit";
    }

    // UPDATE ALUMNI
    @PostMapping("/edit/{id}")
    public String updateAlumni(
            @PathVariable Long id,
            @Valid @ModelAttribute("alumni") Alumni alumni,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "alumni/edit";
        }
        Optional<Alumni> existing = alumniService.getAlumniById(id);
        if (existing.isPresent()) {
            alumni.setId(id);
            alumni.setUser(existing.get().getUser());
            alumni.setCreatedAt(existing.get().getCreatedAt());
            alumniService.updateAlumni(alumni);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Alumni updated successfully!");
        }
        return "redirect:/alumni";
    }

    // DELETE ALUMNI
    @GetMapping("/delete/{id}")
    public String deleteAlumni(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        alumniService.deleteAlumni(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Alumni deleted successfully!");
        return "redirect:/alumni";
    }

    // ALUMNI DIRECTORY
    @GetMapping("/directory")
    public String directory(
            @RequestParam(required = false) String keyword,
            Model model) {
        List<Alumni> alumniList = alumniService.searchAlumni(keyword);
        model.addAttribute("alumniList", alumniList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", alumniList.size());
        return "alumni/directory";
    }

    // MY PROFILE
    @GetMapping("/profile")
    public String myProfile(
            Authentication authentication,
            Model model) {
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            Optional<Alumni> alumni = alumniService
                    .getAlumniByUserId(user.get().getId());
            if (alumni.isPresent()) {
                return "redirect:/alumni/view/"
                        + alumni.get().getId();
            }
        }
        return "redirect:/alumni/add";
    }
}