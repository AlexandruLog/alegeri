package com.example.Alegeri.controller;

import com.example.Alegeri.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContentController {

    private final UserService userService;

    public ContentController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String handleWelcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String handleCustomLogin() {
        return "custom_login";
    }

    @GetMapping("/register")
    public String handleRegister() {
        return "register";
    }

    @GetMapping("/home")
    public String handleHome(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("loggedUser", userService.getAuthenticatedUser(authentication));
        }
        return "home";
    }

    @GetMapping("/user/profile")
    public String handleUserProfile(Authentication authentication, Model model) {
        model.addAttribute("loggedUser", userService.getAuthenticatedUser(authentication));
        return "profile";
    }

    @PostMapping("/edit_description")
    public String handleEditDescription(Authentication authentication, @RequestParam("description") String description) {
        userService.updateUserDescription(authentication, description);
        return "redirect:/user/profile";
    }

    @PostMapping("/apply_candidature")
    public String handleApplyCandidature() {
        userService.addCandidateRole();
        return "redirect:/user/profile";
    }

    @GetMapping("/candidate/candidate_profile")
    public String handleCandidateProfile() {
        return "candidate_profile";
    }
}
