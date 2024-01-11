package com.example.socialweb.controllers.mainControllers;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.requestModels.*;
import com.example.socialweb.models.responseBodies.ProfileBody;
import com.example.socialweb.services.CommunityService;
import com.example.socialweb.services.NewsService;
import com.example.socialweb.services.ReportService;
import com.example.socialweb.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
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
    /*private static User user;*/
    /*private static Community community;*/
    /*private static List<Community> myCommunities;*/

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        List<Community> myCommunities = communityService.getAllCommunitiesByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("news", user.getNews());
        model.addAttribute("myComms", myCommunities);
        return "profile_page";
    }

    @GetMapping("/update")
    public String updateUser(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("update", user);
        return "update_page";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("update") User model, BindingResult bindingResult, Principal principal) {
        userService.addErrorsToBindingResultForUpdate(bindingResult, model);
        if (bindingResult.hasErrors()) {
            return "redirect:/main/update";
        }
        User user = userService.getUserByEmail(principal.getName());
        userService.updateUser(model, passwordEncoder, user);
        return "redirect:/main/profile";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("password", new ChangePassword());
        return "changePassword_page";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute("password") ChangePassword model, Principal principal) {
        if (userService.isRealPassword(model.getConfirmPassword(), principal.getName(), passwordEncoder)) {
            userService.changeUserPassword(model.getNewPassword(), principal.getName(), passwordEncoder);
            return "redirect:/main/profile";
        }
        return "redirect:/main/changePassword";
    }

    @GetMapping("/search")
    public String search(Model model) {
        model.addAttribute("user", new SearchUserModel());
        return "searchUser_page";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute("user") SearchUserModel model, Model model1) {
        model1.addAttribute("users", userService.search(model));
        return "searchUserResult_page";
    }

    @GetMapping("/profile/{id}")
    public String searchProfile(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("news", userService.getUserById(id).getNews());
        model.addAttribute("princ", user);
        model.addAttribute("myComms", communityService.getAllCommunitiesByOwner(user));
        return "searchProfile_page";
    }

    @GetMapping("/add/{id}")
    public String friendshipRequest(@PathVariable("id") Long id, Principal principal) {
        User userTo = userService.getUserById(id);
        User userFrom = userService.getUserByEmail(principal.getName());
        if (!userService.isFriend(userTo, userFrom)) {
            userService.friendRequest(userFrom, userTo);
            return "redirect:/main/profile";
        }
        return "redirect:/main/profile";
    }

    @PostMapping("/friendship/accept/{id}")
    public String positiveFriendshipResponse(@PathVariable("id") Long id, Principal principal) {
        User from = userService.getUserById(id);
        User user = userService.getUserByEmail(principal.getName());
        userService.addToFriend(user, from);
        return "redirect:/main/profile";
    }

    @GetMapping("/friends")
    public String friends(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("friends", user.getFriendList());
        return "friendList_page";
    }

    @GetMapping("/send/{id}")
    public String message(@PathVariable Long id, Model model) {
        model.addAttribute("message", new Message(id));
        return "messageForm_page";
    }

    @PostMapping("/send/{id}")
    public String message(@ModelAttribute("message") Message message, @PathVariable("id") Long id, BindingResult bindingResult, Principal principal) {
        User to = userService.getUserById(id);
        User from = userService.getUserByEmail(principal.getName());
        userService.addErrorsToBindingResultForMessages(from, to, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/main/profile";
        }
        userService.sendMessage(from, to, message);
        return "redirect:/main/profile";
    }

    @GetMapping("/message")
    public String message(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("message", user.getMessageList());
        return "messages_page";
    }

    @GetMapping("/messages/{id}")
    public String messages(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        User userMsg = userService.getUserById(id);
        ArrayList<String> list = user.getMessageList().get(userMsg);
        model.addAttribute("messages", list);
        model.addAttribute("user", userMsg);
        return "chat_page";
    }

    @GetMapping("/create")
    public String createNews(Model model) {
        model.addAttribute("news", new CreateNewsModel());
        return "createNewNews_page";
    }

    @PostMapping("/create")
    public String publicNews(@ModelAttribute("news") CreateNewsModel model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        newsService.publicNews(model, user);
        return "redirect:/main/profile";
    }

    @PostMapping("/like/{id}")
    public String likeNews(@PathVariable("id") Long id, Principal principal) {
        newsService.like(id, principal.getName());
        return "redirect:/main/news";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("news", newsService.getAllNews());
        return "news_page";
    }

    @GetMapping("/comment/{id}")
    public String commentNews(Model model, @PathVariable("id") Long id) {
        model.addAttribute("news", new CommentModel(id));
        return "commentNews_page";
    }

    @PostMapping("/comment/{id}")
    public String commentNews(@ModelAttribute("news") CommentModel model, @PathVariable("id") Long id, BindingResult bindingResult, Principal principal) {
        model.setId(id);
        newsService.checkCommentValid(bindingResult, model);
        if (!bindingResult.hasErrors()) {
            newsService.comment(model, principal.getName());
            return "redirect:/main/news";
        }
        return "redirect:/main/news";

    }

    @GetMapping("/comments/{id}")
    public String comments(@PathVariable("id") Long id, Model model) {
        model.addAttribute("news", newsService.getNewsById(id).getComments());
        return "newsComments_page";
    }

    @GetMapping("/report/news/{id}")
    public String newsReport(@PathVariable("id") Long id, Model model) {
        ReportModel reportModel = new ReportModel(id);
        model.addAttribute("newsReport", reportModel);
        return "reportNews_page";
    }

    @PostMapping("/report/news/{id}")
    public String report(@PathVariable("id") Long id, @ModelAttribute("newsReport") ReportModel model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        News news = newsService.getNewsById(id);
        reportService.reportNews(user, news, model);
        return "redirect:/main/news";
    }

    @PostMapping("/delete/{id}")
    public String deleteFriend(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        User friend = userService.getUserById(id);
        userService.deleteFriend(user, friend);
        return "redirect:/main/friends";
    }

    @GetMapping("/community/create")
    public String createCommunity(Model model) {
        model.addAttribute("comm", new CommunityModel());
        return "createCommunity_page";
    }

    @PostMapping("/community/create")
    public String createCommunity(@ModelAttribute("comm") CommunityModel model, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            communityService.createCommunity(model, user);
        } catch (Exception e) {
            log.info("community: " + e.getMessage());
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/report/community/{id}")
    public String reportCommunity(@PathVariable("id") Long id, Model model) {
        model.addAttribute("repModel", new ReportModel(id));
        return "reportCommunity_page";
    }

    @PostMapping("/report/community/{id}")
    public String reportCommunity(@PathVariable("id") Long id, @ModelAttribute("repModel") ReportModel model, Principal principal) {
        try {
            Community community = communityService.getCommunityById(id);
            User user = userService.getUserByEmail(principal.getName());
            reportService.reportCommunity(community, model, user);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/search")
    public String searchCommunity(Model model) {
        model.addAttribute("comSModel", new CommunitySearchModel());
        return "communitySearch_page";
    }

    @PostMapping("/community/search")
    public String searchCommunity(@ModelAttribute("comSModel") CommunitySearchModel Rmodel, Model model) {
        try {
            Community foundCommunity = communityService.findCommunityForUser(Rmodel);
            model.addAttribute("community", foundCommunity);
            return "searchCommunityResult_page";
        } catch (Exception e) {
            log.info(e.getMessage());
            return "redirect:/main/profile";
        }
    }

    @PostMapping("/community/delete/{id}")
    public String deleteCommunity(@PathVariable("id") Long id) {
        communityService.delete(id);
        return "redirect:/main/profile";
    }

    @GetMapping("/community")
    public String getUserCommunities(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("comms", communityService.getAllCommunitiesByOwner(user));
        return "userCommunities_page";
    }

    @GetMapping("/community/{id}")
    public String community(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Community community = communityService.getCommunityById(id);
        model.addAttribute("comm", community);
        model.addAttribute("user", user);
        return "community_page";
    }

    @GetMapping("/community/message/{id}")
    public String userToCommunityMessage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("message", new Message(id));
        return "communityMessage_page";
    }

    @PostMapping("/community/message/{id}")
    public String userToCommunityMessage(@PathVariable("id") Long id, @ModelAttribute("message") Message message, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            Community community = communityService.getCommunityById(id);
            communityService.userMessage(message, user, community);
        } catch (Exception e) {
            log.info("community: " + e.getMessage());
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/community/all")
    public String allCommunities(Model model) {
        model.addAttribute("comms", communityService.getAllCommunityByMode(CommunityMode.PUBLIC_MODE));
        return "allCommunities_page";
    }

    @PostMapping("/community/subscribe/{id}")
    public String subscribe(@PathVariable("id") Long id, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            communityService.subscribe(id, user);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/main/community/all";
    }

    @PostMapping("/community/unsubscribe/{id}")
    public String unsubscribe(@PathVariable("id") Long id, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            communityService.unsubscribe(id, user);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/main/community/all";
    }

    @GetMapping("/community/invite/{id}")
    public String invite(@PathVariable("id") Long userId, Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("userComms", communityService.getAllCommunitiesByOwner(user));
        model.addAttribute("userId", userId);
        return "communityInviteChoice_page";
    }

    @PostMapping("/community/invite/{id}/{commId}")
    public String invite(@PathVariable("id") Long userId, @PathVariable("commId") Long commId) {
        try {
            communityService.invite(userId, commId);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/main/profile";
    }

    @GetMapping("/report/user/{id}")
    public String userReport(@PathVariable("id") Long id, Model model) {
        model.addAttribute("repModel", new ReportModel(id));
        return "reportUser_page";
    }

    @PostMapping("/report/user/{id}")
    public String userReport(@ModelAttribute("repModel") ReportModel model, @PathVariable("id") Long id, Principal principal) {
        try {
            User user = userService.getUserByEmail(principal.getName());
            reportService.reportUser(model, id, user);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return "redirect:/main/profile";
    }
}