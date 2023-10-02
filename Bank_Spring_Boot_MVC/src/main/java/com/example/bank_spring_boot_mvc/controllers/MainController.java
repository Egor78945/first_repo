package com.example.bank_spring_boot_mvc.controllers;

import com.example.bank_spring_boot_mvc.entities.Transaction;
import com.example.bank_spring_boot_mvc.entities.User;
import com.example.bank_spring_boot_mvc.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;
    @GetMapping("/main")
    public String mainPage(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "main";
    }
    @GetMapping("/pay")
    public String payMoney(Model model){
        model.addAttribute("transaction", new Transaction());
        return "pay";
    }
    @PostMapping("/pay")
    public String payMoney(Principal principal, @ModelAttribute("transaction") Transaction transaction){
        User user = userService.getUserByEmail(principal.getName());
        userService.payMoney(transaction, user);
        return "redirect:/main";
    }
    @GetMapping("/take")
    public String takeMoney(Model model){
        model.addAttribute("transaction", new Transaction());
        return "take";
    }
    @PostMapping("/take")
    public String takeMoney(Principal principal, @ModelAttribute("transaction") Transaction transaction){
        User user = userService.getUserByEmail(principal.getName());
        userService.takeMoney(transaction, user);
        return "redirect:/main";
    }
}