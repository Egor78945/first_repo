package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.models.entities.Message;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.*;
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
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final NewsService newsService;
    private final CommentService commentService;
    private User user;

    @RequestMapping
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (user == null)
            user = userService.getCurrentUser(principal);
        model.addAttribute("user", user);
        model.addAttribute("news", newsService.getAllByPublisherId(user.getId()));
        return "profile_page";
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model, Principal principal) {
        if (!user.getId().equals(id) && !user.getIsBan()) {
            model.addAttribute("user_profile", userService.getUserById(id));
            model.addAttribute("user", userService.getCurrentUser(principal));
            return "search_user_profile_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/settings")
    public String settings() {
        if (!user.getIsBan())
            return "settings_page";
        else
            return "redirect:/main/profile";
    }

    @GetMapping("/settings/password")
    public String password(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("password", new PasswordSettingsModel());
            return "password_settings_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/settings/password")
    public String password(@ModelAttribute("password") PasswordSettingsModel model) {
        if (userService.changePassword(model, user, passwordEncoder) || user.getIsBan())
            return "redirect:/main/profile";
        else
            return "redirect:/main/settings";
    }

    @GetMapping("/settings/profile")
    public String profile(Model model) {
        if (!user.getIsBan()) {
            ProfileSettingsModel profileModel = new ProfileSettingsModel();
            model.addAttribute("profile", profileModel.build(user));
            return "profile_settings_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/settings/profile")
    public String profile(@ModelAttribute("profile") ProfileSettingsModel model) {
        if (userService.updateProfile(model, user) || user.getIsBan())
            return "redirect:/main/profile";
        else
            return "redirect:/main/settings";
    }

    @GetMapping("/user/search")
    public String userSearch(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("search", new UserSearchModel());
            return "search_user_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/user/search")
    public String userSearch(@ModelAttribute("search") UserSearchModel searchModel, Model model) {
        if (!user.getIsBan()) {
            List<User> users = null;
            try {
                users = userService.search(searchModel);
                model.addAttribute("users", users);
                return "search_user_result_page";
            } catch (Exception e) {
                log.info(e.getMessage());
                return "redirect:/main/user/search";
            }
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/user/friends")
    public String userFriends(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("friends", user.getFriends());
            return "user_friends_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/user/friends/add/{id}")
    public String addFriend(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan()) {
            userService.addFriend(userService.getCurrentUser(principal), userService.getUserById(id));
            user = null;
        }
        return "redirect:/main/profile";
    }

    @PostMapping("/user/friends/remove/{id}")
    public String removeFriend(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan()) {
            userService.removeFriend(userService.getCurrentUser(principal), userService.getUserById(id));
            user = null;
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/message/send/{id}")
    public String messageTo(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("message", new MessageBody(id));
            return "message_to_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/message/send/{id}")
    public String messageTo(@PathVariable("id") Long id, @ModelAttribute("message") MessageBody body, Principal principal) {
        if (!user.getIsBan())
            messageService.sendMessageTo(body, userService.getCurrentUser(principal), id);
        return "redirect:/main/profile";
    }

    @GetMapping("/messages")
    public String messages(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("messages", user.getMessages());
            return "messages_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/messages/{id}")
    public String messages(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            List<Message> list = messageService.getAllBySenderIdAndRecipientId(id, user.getId());
            model.addAttribute("messages", list);
            return "messages_by_user_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/news")
    public String newsMenu() {
        if (!user.getIsBan())
            return "news_menu_page";
        else
            return "redirect:/main/profile";
    }

    @GetMapping("/news/all")
    public String news(Model model, Principal principal) {
        if (!user.getIsBan()) {
            model.addAttribute("news", newsService.getAllNews());
            model.addAttribute("user", userService.getCurrentUser(principal));
            return "news_page";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/news/post")
    public String postNews(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("model", new PostNewsModel());
            return "post_news_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/post")
    public String postNews(@ModelAttribute("model") PostNewsModel model, Principal principal) {
        if (!user.getIsBan()) {
            newsService.postNews(model, userService.getCurrentUser(principal));
            return "redirect:/main/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/like/{id}")
    public String like(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan()) {
            newsService.like(newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/main/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/unlike/{id}")
    public String unlike(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan()) {
            newsService.unlike(newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/main/news/all";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/news/comment/{id}")
    public String comment(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("comment", new CommentNewsModel(id));
            return "comment_news_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/comment/{id}")
    public String comment(@PathVariable("id") Long id, @ModelAttribute("comment") CommentNewsModel model, Principal principal) {
        if (!user.getIsBan()) {
            commentService.comment(model, newsService.getNewsById(id), userService.getCurrentUser(principal));
            return "redirect:/main/news/all";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/news/comments/{id}")
    public String comments(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("comments", newsService.getNewsById(id).getComments());
            model.addAttribute("user", user);
            return "news_comments_page";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        if (!user.getIsBan()) {
            commentService.deleteComment(commentService.getCommentById(id), user);
            return "redirect:/main/news/all";
        } else
            return "redirect:/main/profile";
    }

    @PostMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Long id) {
        if (!user.getIsBan())
            newsService.deleteNews(newsService.getNewsById(id), user);
        return "redirect:/main/profile";
    }
}