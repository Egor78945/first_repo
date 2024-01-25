package com.example.socialweb.controllers.adminControllers;

import com.example.socialweb.models.entities.BanDetails;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final BanDetailsService banDetailsService;
    private static User user;

    @GetMapping("/panel")
    public String adminPanel(Principal principal, Model model) {
        if (user == null)
            user = userService.getCurrentUser(principal);
        model.addAttribute("user", user);
        return "admin_panel_page";
    }
    @GetMapping("/user/banned")
    public String bannedUsers(Model model){
        model.addAttribute("banned", userService.getAllBannedUsers());
        return "all_banned_users_page";
    }
    @GetMapping("/user/banned/{id}")
    public String banDetails(@PathVariable("id") Long id, Model model){
        BanDetails bd = banDetailsService.getBanDetailsByUserId(id);
        if(bd != null) {
            model.addAttribute("banDetails", bd);
            return "ban_details_page";
        }
        return "redirect:/admin/user/banned";
    }
    @PostMapping("/user/unban/{id}")
    public String unbanUser(@PathVariable("id") Long id){
        banDetailsService.unban(id);
        return "redirect:/admin/user/banned";
    }
}
