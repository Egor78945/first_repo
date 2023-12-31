package com.example.bank_spring_boot_mvc.controllers;

import com.example.bank_spring_boot_mvc.entities.Transaction;
import com.example.bank_spring_boot_mvc.entities.UpdateUserModel;
import com.example.bank_spring_boot_mvc.entities.User;
import com.example.bank_spring_boot_mvc.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MainController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/main")
    public String mainPage(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/pay")
    public String payMoney(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "pay";
    }

    @PostMapping("/pay")
    public String payMoney(Principal principal, @ModelAttribute("transaction") Transaction transaction) {
        User user = userService.getUserByEmail(principal.getName());
        userService.payMoney(transaction, user);
        return "redirect:/user/main";
    }

    @GetMapping("/take")
    public String takeMoney(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "take";
    }

    @PostMapping("/take")
    public String takeMoney(Principal principal, @ModelAttribute("transaction") Transaction transaction) {
        User user = userService.getUserByEmail(principal.getName());
        userService.takeMoney(transaction, user);
        return "redirect:/user/main";
    }

    @GetMapping("/transfer")
    public String transfer(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(Principal principal, @ModelAttribute("transaction") Transaction transaction) {
        User userFrom = userService.getUserByEmail(principal.getName());
        User userTo = userService.getUserByEmail(transaction.getEmail());
        userService.transferMoney(userFrom, userTo, transaction);
        return "redirect:/user/main";
    }

    @GetMapping("/update")
    public String updateUserData(Model model) {
        model.addAttribute("update", new UpdateUserModel());
        return "update_panel";

    }

    @PostMapping("/update")
    public String updateUserData(Principal principal, @ModelAttribute("update") UpdateUserModel userModel) throws BadCredentialsException {
        User user = userService.getUserByEmail(principal.getName());
        if (userModel.getName().equals(user.getName()) || userModel.getName().isEmpty()) {
            throw new BadCredentialsException("Name must not to be null and must not to be equals old name.");
        } else if (userModel.getEmail().equals(user.getEmail()) || userModel.getEmail().isEmpty()) {
            throw new BadCredentialsException("Email must not to be equals old email and must not to be null.");
        }
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userService.updateUser(user);
        return "redirect:/user/main";
    }
}