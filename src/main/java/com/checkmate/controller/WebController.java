package com.checkmate.controller;

import com.checkmate.model.User;
import com.checkmate.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

;
/**
 * WebController class to handle web requests related to user management.
 */
@Controller
@RequestMapping("/")
public class WebController {
    @Autowired
    private UserService userService;
    /**
     * Method to display the user creation form.
     *
     * @param model the model to add attributes to.
     * @return the name of the Thymeleaf template.
     */
    @GetMapping("/")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User()); // Empty User object for the form
        return "create-user"; // Name of the Thymeleaf template
    }
    /**
     * Method to handle the submission of the user creation form.
     *
     * @param user the user object submitted from the form.
     * @param session the HTTP session to store user data.
     * @return a redirect to the create form after submission.
     */
    
    @PostMapping("/users/create")
    public String showCreateForm(User user, HttpSession session) {
        User createdUser = userService.saveUser(user);
        session.setAttribute("user", createdUser); // Store the created user in the session
        return "redirect:/home"; // Redirect after submission
    }
    /**
     * Method to handle the user login.
     *
     * @param user the user object submitted from the form.
     * @param session the HTTP session to store user data.
     * @return a redirect to the home page after login.
     */
    @GetMapping("/home")
    public String showHome(Model model, HttpSession session) {
        return "home"; // Name of the Thymeleaf template
    }

    /**
     * Method to display the user login form.
     *
     * @param model the model to add attributes to.
     * @return the name of the Thymeleaf template.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/create";
    }
}