package com.example.socialweb.controllers.adminControllers;

import com.example.socialweb.models.entities.BanDetails;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.BanUserModel;
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
    private final ReportService reportService;
    private static User user;

    @GetMapping("/panel")
    public String adminPanel(Principal principal, Model model) {
        if (user == null)
            user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            model.addAttribute("user", user);
            return "admin_panel_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/banned")
    public String bannedUsers(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("banned", userService.getAllBannedUsers());
            return "all_banned_users_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/banned/{id}")
    public String banDetails(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            BanDetails bd = banDetailsService.getBanDetailsByUserId(id);
            if (bd != null) {
                model.addAttribute("banDetails", bd);
                return "ban_details_page";
            }
            return "redirect:/admin/user/banned";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/user/unban/{id}")
    public String unbanUser(@PathVariable("id") Long id) {
        if (!user.getIsBan()) {
            banDetailsService.unban(id);
            return "redirect:/admin/user/banned";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/ban/{id}")
    public String banUser(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("banModel", new BanUserModel(id));
            return "ban_user_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/user/ban/{id}")
    public String banUser(@PathVariable("id") Long id, @ModelAttribute("banModel") BanUserModel model, Principal principal) {
        if (!user.getIsBan()) {
            banDetailsService.ban(userService.getUserById(id), userService.getCurrentUser(principal), model);
            return "redirect:/admin/panel";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/reports")
    public String reportedUsers(Model model) {
        model.addAttribute("reports", reportService.getAllReportedUsers());
        return "reported_users_page";
    }
}
