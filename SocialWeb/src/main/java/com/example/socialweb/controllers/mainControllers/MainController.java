package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.Message;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.*;
import com.example.socialweb.services.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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
    private final ReportService reportService;
    private final CommunityService communityService;
    private User user;
    private List<Community> ownCommunities;

    @RequestMapping
    public String mainPage() {
        return "main_page";
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        if (user == null) {
            user = userService.getCurrentUser(principal);
            ownCommunities = communityService.getAllCommunitiesByOwner(user);
        }
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

    @GetMapping("/user/report/{id}")
    public String reportUser(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("report", new ReportModel(id));
            return "report_page";
        }
        return "redirect:/main/profile";
    }

    @PostMapping("/user/report/{id}")
    public String reportUser(@PathVariable("id") Long id, @ModelAttribute("report") ReportModel reportModel, Principal principal) {
        if (!user.getIsBan())
            reportService.reportUser(userService.getUserById(id), reportModel, userService.getCurrentUser(principal));
        return "redirect:/main/profile";
    }

    @GetMapping("/community/menu")
    public String commMenu(Model model, Principal principal) {
        if (!user.getIsBan()) {
            model.addAttribute("userComms", userService.getCurrentUser(principal).getCommunities());
            model.addAttribute("userOwnComms", ownCommunities);
            return "community_menu_page";
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/create")
    public String createComm(Model model) {
        if (!user.getIsBan() && ownCommunities.size() < 3) {
            model.addAttribute("commModel", new CommunityModel());
            return "create_community_page";
        }
        return "redirect:/main/profile";
    }

    @PostMapping("/community/create")
    public String createComm(@ModelAttribute("commModel") CommunityModel communityModel, Principal principal) {
        if (!user.getIsBan() && communityService.createCommunity(communityModel, userService.getCurrentUser(principal), ownCommunities)) {
            ownCommunities = communityService.getAllCommunitiesByOwner(userService.getCurrentUser(principal));
            return "redirect:/main/community/my";
        } else
            return "redirect:/main/profile";
    }

    @GetMapping("/community/my/own")
    public String ownCommunities(Model model, Principal principal) {
        if (!user.getIsBan()) {
            User user = userService.getCurrentUser(principal);
            model.addAttribute("communities", communityService.getAllCommunitiesByOwner(user));
            model.addAttribute("user", user);
            return "my_communities_page";
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/my")
    public String myCommunities(Model model, Principal principal) {
        if (!user.getIsBan()) {
            model.addAttribute("communities", userService.getCurrentUser(principal).getCommunities());
            model.addAttribute("user", user);
            return "my_communities_page";
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/{id}")
    public String communityProfile(@PathVariable("id") Long id, Model model, Principal principal) {
        if (!user.getIsBan()) {
            User user = userService.getCurrentUser(principal);
            model.addAttribute("community", communityService.getCommunityById(id));
            model.addAttribute("user", user);
            return "community_profile_page";
        }
        return "redirect:/main/profile";
    }

    @PostMapping("/community/subscribe/{id}")
    public String subscribe(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan() && communityService.subscribe(communityService.getCommunityById(id), userService.getCurrentUser(principal)))
            return "redirect:/main/community/my";
        else
            return "redirect:/main/community/menu";
    }

    @PostMapping("/community/unsubscribe/{id}")
    public String unsubscribe(@PathVariable("id") Long id, Principal principal) {
        if (!user.getIsBan() && communityService.unsubscribe(communityService.getCommunityById(id), userService.getCurrentUser(principal)))
            return "redirect:/main/community/my";
        else
            return "redirect:/main/community/menu";
    }

    @GetMapping("/community")
    public String community(Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("communities", communityService.getAllCommunities());
            return "all_communities_page";
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/news/post/{id}")
    public String communityNews(@PathVariable("id") Long id, Model model) {
        if (!user.getIsBan()) {
            model.addAttribute("model", new PostNewsModel(id));
            return "post_news_page";
        } else
            return "redirect:/main/profile";
    }
}