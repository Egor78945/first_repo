package com.example.bank_spring_boot_mvc.controllers;


import com.example.bank_spring_boot_mvc.entities.Role;
import com.example.bank_spring_boot_mvc.entities.User;
import com.example.bank_spring_boot_mvc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/reg")
    public String registration(@ModelAttribute("user") User user) throws BadCredentialsException{
        if (userService.existsUserByEmail(user.getEmail())) {
            throw new BadCredentialsException("User with email " + user.getEmail() + " already exists, choose a different email.");
        } else {
            User regUser = new User();
            regUser.setName(user.getName());
            regUser.setEmail(user.getEmail());
            regUser.setPassword(passwordEncoder.encode(user.getPassword()));
            regUser.setDateOfRegistration(new Date(System.currentTimeMillis()).toString());
            regUser.setRole(Role.ADMIN_ROLE);
            regUser.setBalance(0L);
            userService.saveUser(regUser);
            return "redirect:/user/main";
        }

    }

}