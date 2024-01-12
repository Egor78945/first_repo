package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.services.CommunityService;
import com.example.socialweb.services.NewsService;
import com.example.socialweb.services.ReportService;
import com.example.socialweb.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {

        return "profile_page";
    }

}