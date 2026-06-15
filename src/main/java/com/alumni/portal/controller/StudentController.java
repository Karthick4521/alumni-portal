package com.alumni.portal.controller;

import com.alumni.portal.entity.Student;
import com.alumni.portal.entity.User;
import com.alumni.portal.service.StudentService;
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
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    // LIST ALL STUDENTS
    @GetMapping("")
    public String listStudents(
            @RequestParam(required = false) String keyword,
            Model model) {
        List<Student> studentList = studentService.searchStudents(keyword);
        model.addAttribute("studentList", studentList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalCount", studentList.size());
        return "student/list";
    }

    // SHOW ADD FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        return "student/add";
    }

    // SAVE NEW STUDENT
    @PostMapping("/add")
    public String saveStudent(
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "student/add";
        }

        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        user.ifPresent(student::setUser);

        studentService.saveStudent(student);
        redirectAttributes.addFlashAttribute("successMsg",
                "Student added successfully!");
        return "redirect:/students";
    }

    // VIEW STUDENT
    @GetMapping("/view/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isEmpty()) {
            return "redirect:/students";
        }
        model.addAttribute("student", student.get());
        return "student/view";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isEmpty()) {
            return "redirect:/students";
        }
        model.addAttribute("student", student.get());
        return "student/edit";
    }

    // UPDATE STUDENT
    @PostMapping("/edit/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @Valid @ModelAttribute("student") Student student,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "student/edit";
        }

        Optional<Student> existing = studentService.getStudentById(id);
        if (existing.isPresent()) {
            student.setId(id);
            student.setUser(existing.get().getUser());
            student.setCreatedAt(existing.get().getCreatedAt());
            studentService.updateStudent(student);
            redirectAttributes.addFlashAttribute("successMsg",
                    "Student updated successfully!");
        }
        return "redirect:/students";
    }

    // DELETE STUDENT
    @GetMapping("/delete/{id}")
    public String deleteStudent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("successMsg",
                "Student deleted successfully!");
        return "redirect:/students";
    }
}