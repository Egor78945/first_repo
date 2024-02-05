package com.example.socialweb.controllers.mainControllers;


import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.CommentNewsModel;
import com.example.socialweb.models.requestModels.PostNewsModel;
import com.example.socialweb.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final NewsService newsService;
    private final ReportService reportService;
    private final CommunityService communityService;
    private final CommentService commentService;

    @GetMapping("/menu")
    public String newsMenu(Principal principal) {
        if (!userService.getCurrentUser(principal).getIsBan())
            return "news_menu_page";
        else
            return "redirect:/main/profile";
    }

    @GetMapping("/all")
    public String news(Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            model.addAttribute("news", newsService.getAllNews());
            model.addAttribute("user", userService.getCurrentUser(principal));
            return "news_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/post")
    public String postNews(Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            model.addAttribute("model", new PostNewsModel());
            return "post_news_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/user/post")
    public String postNews(@ModelAttribute("model") PostNewsModel model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            newsService.postNews(model, userService.getCurrentUser(principal));
            return "redirect:/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            newsService.like(newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/unlike/{id}")
    public String unlike(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            newsService.unlike(newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/news/all";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/comment/{id}")
    public String comment(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            model.addAttribute("comment", new CommentNewsModel(id));
            return "comment_news_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/comment/{id}")
    public String comment(@PathVariable("id") Long id, @ModelAttribute("comment") CommentNewsModel model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            commentService.comment(model, newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/news/all";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/comments/{id}")
    public String comments(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            model.addAttribute("comments", newsService.getNewsById(id).getComments());
            model.addAttribute("user", user);
            return "news_comments_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan()) {
            commentService.deleteComment(commentService.getCommentById(id), user);
            return "redirect:/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteNews(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getCurrentUser(principal);
        if (!user.getIsBan())
            newsService.deleteNews(newsService.getNewsById(id), user);
        return "redirect:/main/profile";
    }
}
