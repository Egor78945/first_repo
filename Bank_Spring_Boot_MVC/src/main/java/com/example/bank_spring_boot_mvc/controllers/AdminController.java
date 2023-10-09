package com.example.bank_spring_boot_mvc.controllers;

import com.example.bank_spring_boot_mvc.entities.Role;
import com.example.bank_spring_boot_mvc.entities.UpdateRoleModel;
import com.example.bank_spring_boot_mvc.entities.User;
import com.example.bank_spring_boot_mvc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/find")
    public String adminMenu(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("find", new User());
        return "admin_main";
    }

    @GetMapping("/admin_panel")
    public String adminUserPanel(Model model, @ModelAttribute("find") User user) {
        User user1 = userService.getUserByEmail(user.getEmail());
        model.addAttribute("user", user1);
        return "admin_panel";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) throws RuntimeException {
        User user1 = userService.getUserById(id);
        if (user1.getRole().equals(Role.ADMIN_ROLE)) {
            throw new RuntimeException("Admin can't delete admin");
        } else {
            userService.deleteUser(user1);
        }
        return "redirect:/user/main";
    }

    @GetMapping("/grant")
    public String grant(Model model) {
        model.addAttribute("user", new UpdateRoleModel());
        return "update_role";
    }

    @PostMapping("/grant")
    public String grant(@ModelAttribute("user") UpdateRoleModel model) {
        User user = userService.getUserByEmail(model.getEmail());
        user.setRole(Role.ADMIN_ROLE);
        userService.updateUser(user);
     return "redirect:/user/main";
    }
}

