package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.PasswordSettingsModel;
import com.example.socialweb.models.requestModels.ProfileSettingsModel;
import com.example.socialweb.models.requestModels.UserSearchModel;
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
import java.util.List;

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
    private User user;

    @RequestMapping
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (user == null)
            user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "profile_page";
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.getUserById(id));
        return "search_user_profile_page";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings_page";
    }

    @GetMapping("/settings/password")
    public String password(Model model) {
        model.addAttribute("password", new PasswordSettingsModel());
        return "password_settings_page";
    }

    @PostMapping("/settings/password")
    public String password(@ModelAttribute("password") PasswordSettingsModel model) {
        if (userService.changePassword(model, user, passwordEncoder))
            return "redirect:/main/profile";
        else
            return "redirect:/main/settings";
    }

    @GetMapping("/settings/profile")
    public String profile(Model model) {
        ProfileSettingsModel profileModel = new ProfileSettingsModel();
        model.addAttribute("profile", profileModel.build(user));
        return "profile_settings_page";
    }

    @PostMapping("/settings/profile")
    public String profile(@ModelAttribute("profile") ProfileSettingsModel model) {
        System.out.println(user);
        if (userService.updateProfile(model, user))
            return "redirect:/main/profile";
        else
            return "redirect:/main/settings";
    }
    @GetMapping("/user/search")
    public String userSearch(Model model) {
        model.addAttribute("search", new UserSearchModel());
        return "search_user_page";
    }
    @PostMapping("/user/search")
    public String userSearch(@ModelAttribute("search") UserSearchModel searchModel, Model model){
        List<User> users = null;
        try {
            users = userService.search(searchModel);
            model.addAttribute("users", users);
            return "search_user_result_page";
        } catch (Exception e) {
            log.info(e.getMessage());
            return "redirect:/main/user/search";
        }
       /* if(users.isEmpty())
            return "redirect:/main/user/search";
        else
            model.addAttribute("users", users);
        return "search_user_result_page";*/
    }
}