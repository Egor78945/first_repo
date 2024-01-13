package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
@Slf4j
public class MainController {
    private final UserService userService;
    private final NewsService newsService;
    private final ReportService reportService;
    private final PasswordEncoder passwordEncoder;
    private final CommunityService communityService;

    @RequestMapping
    public String mainPage(){
        return "main_page";
    }
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        model.addAttribute("user", principal.getName());
        return "profile_page";
    }

}