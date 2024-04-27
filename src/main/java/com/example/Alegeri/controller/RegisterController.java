package com.example.Alegeri.controller;

import com.example.Alegeri.model.MyUser;
import com.example.Alegeri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/submit_register")
    public String handleSubmitRegister(@RequestParam("first_name") String firstName,
                                       @RequestParam("last_name") String lastName,
                                       @RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirm_password") String confirmPassword,
                                       Model model) {
        if (password.equals(confirmPassword)) {
            if (username.equals(userService.getUserByUsername(username))) {
                model.addAttribute("usernameMatchingError", "Numele de utilizator '" + username + "' exista deja! Incearca altul.");
            } else {
                MyUser newUser = new MyUser(firstName, lastName, username, password);
                userService.createUser(newUser);
                model.addAttribute("successMessage", "Contul tau a fost creat cu succes!");
            }
        } else {
            model.addAttribute("passwordMatchingError", "Parolele nu coincid! Incearca din nou.");
        }
        return "register";
    }
}
