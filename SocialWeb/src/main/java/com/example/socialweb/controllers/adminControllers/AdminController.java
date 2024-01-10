package com.example.socialweb.controllers.adminControllers;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.BanUserBody;
import com.example.socialweb.models.requestModels.SearchUserModel;
import com.example.socialweb.services.AdminService;
import com.example.socialweb.services.NewsService;
import com.example.socialweb.services.ReportService;
import com.example.socialweb.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final NewsService newsService;
    private final ReportService reportService;
    private final AdminService adminService;

    @GetMapping("/panel")
    public String adminPanel(Model model, Principal principal){
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        model.addAttribute("newsReports", newsService.getAllReportNews());
        model.addAttribute("allReports", reportService.getAllReports());
        model.addAttribute("allBanned", userService.getAllBannedUsers());
        model.addAttribute("allReportsUsers", userService.getAllReportedUsers());
        return "adminPanel_page";
    }
    @GetMapping("/reports/news")
    public String newsReports(Model model){
        model.addAttribute("list", newsService.getAllReportNews());
        return "newsReports_page";
    }
    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable("id") Long id){
        newsService.deleteNews(id);
        return "redirect:/admin/reports/news";
    }
    @GetMapping("/ban/{id}")
    public String banUser(@PathVariable("id") Long id, Model model){
        model.addAttribute("model", new BanUserBody());
        model.addAttribute("id", id);
        return "ban_page";
    }
    @PostMapping("/ban/{id}")
    public String banUser(@PathVariable("id") Long id,@ModelAttribute("model") BanUserBody model,Principal principal){
        adminService.banUser(userService.getUserById(id), userService.getUserByEmail(principal.getName()), model.getReason());
        return "redirect:/admin/panel";
    }
    @PostMapping("/unban/{id}")
    public String unbanUser(@PathVariable("id") Long id,@ModelAttribute("model") BanUserBody model,Principal principal){
        adminService.unbanUser(userService.getUserById(id), userService.getUserByEmail(principal.getName()));
        return "redirect:/admin/panel";
    }
    @GetMapping("/banned")
    public String banned(Model model){
        model.addAttribute("banned", userService.getAllBannedUsers());
        return "bannedList_page";
    }
    @GetMapping("/reports/users")
    public String userReports(Model model){
        model.addAttribute("userReports", userService.getAllReportedUsers());
        return "usersReports_page";
    }


}
