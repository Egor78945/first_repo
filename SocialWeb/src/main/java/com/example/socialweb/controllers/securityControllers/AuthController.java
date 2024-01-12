package com.example.socialweb.controllers.securityControllers;

import com.example.socialweb.models.requestModels.RegisterBody;
import com.example.socialweb.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login_page";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterBody());
        return "register_page";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterBody body) {
        if (!userService.userDataIsValid(body)) {
            return "redirect:/auth/register";
        } else {
            userService.register(body, passwordEncoder);
            return "redirect:/main/profile";
        }
    }
}
