package com.example.socialweb.services;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.Report;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.requestModels.CommunityModel;
import com.example.socialweb.models.requestModels.CommunitySearchModel;
import com.example.socialweb.models.requestModels.Message;
import com.example.socialweb.models.requestModels.ReportModel;
import com.example.socialweb.repositories.CommunityRepository;
import com.example.socialweb.repositories.ReportRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public Community getCommunityById(Long id) {
        return communityRepository.findCommunityById(id);
    }

    public void createCommunity(CommunityModel model, User user) throws Exception {
        if (userCanCreateCommunity(user)) {
            Community community = new Community();
            community.setOwner(user);
            community.setMode(model.getMode());
            community.setDescription(model.getDescription());
            community.setName(model.getName());
            communityRepository.save(community);
            log.info("community: community successfully created.");
        } else {
            throw new Exception("user already have 3 communities.");
        }
    }

    public boolean userCanCreateCommunity(User user) {
        List<Community> list = communityRepository.findAllByOwner(user);
        if (list.size() < 3) {
            log.info("community: user can create a community, community count: " + list.size() + ".");
            return true;
        } else {
            log.info("community: user can not create a community, community count: " + list.size() + ".");
            return false;
        }
    }

    public List<Community> getAllCommunitiesByOwner(User user) {
        return communityRepository.findAllByOwner(user);
    }

    public void userMessage(Message message, User user, Community community) throws Exception {
        if (checkMessage(message.getMessage())) {
            if (community.getMessages().containsKey(user)) {
                community.getMessages().get(user).add(message.getMessage());
                communityRepository.save(community);
                log.info("community: message successfully sent.");
            } else {
                ArrayList<String> messages = new ArrayList<>();
                messages.add(message.getMessage());
                community.getMessages().put(user, messages);
                communityRepository.save(community);
                log.info("community: message successfully sent.");
            }
        } else {
            throw new Exception("community: message is not sent.");
        }
    }

    public boolean checkMessage(String message) {
        if (message.isEmpty() || message.length() > 200) {
            log.info("community: message is invalid.");
            return false;
        }
        log.info("community: message is valid.");
        return true;
    }

    public List<Community> getAllCommunityByMode(CommunityMode mode) {
        return communityRepository.findAllByMode(mode);
    }

    public void subscribe(Long commId, User user) throws Exception {
        Community community = getCommunityById(commId);
        if (user.getCommunities().contains(community)) {
            throw new Exception("community: user with id " + user.getId() + " is already signed on community with id " + commId + ".");
        } else {
            user.getCommunities().add(community);
            community.getSubscribers().add(user);
            userRepository.save(user);
            communityRepository.save(community);
            log.info("community: user with id " + user.getId() + " subscribed on community with id " + commId + ".");
        }
    }

    public void unsubscribe(Long commId, User user) throws Exception {
        Community community = getCommunityById(commId);
        if (!user.getCommunities().contains(community)) {
            throw new Exception("community: user with id " + user.getId() + " is not subscribed on community with id " + commId + ".");
        } else {
            user.getCommunities().remove(community);
            community.getSubscribers().remove(user);
            userRepository.save(user);
            communityRepository.save(community);
            log.info("community: user with id " + user.getId() + " subscribed from community with id " + commId + ".");
        }
    }

    public void invite(Long userId, Long commId) throws Exception {
        Community community = getCommunityById(commId);
        User user = userRepository.findUserById(userId);
        if (user.getCommunities().contains(community)) {
            throw new Exception("community: user with id " + user.getId() + " already consists in community " + community.getName() + ".");
        } else if (user.getMessageList().containsKey(community.getOwner())) {
            community.getSubscribers().add(user);
            communityRepository.save(community);
            user.getMessageList().get(community.getOwner()).add("I added you in my community - " + community.getName() + ".");
            user.getCommunities().add(community);
            userRepository.save(user);
            log.info("community: user with id " + user.getId() + " has been added to community with id " + community.getId());
        } else {
            ArrayList<String> list = new ArrayList<>();
            list.add("I added you in my community - " + community.getName() + ".");
            community.getSubscribers().add(user);
            communityRepository.save(community);
            user.getMessageList().put(community.getOwner(), list);
            user.getCommunities().add(community);
            userRepository.save(user);
            log.info("community: user with id " + user.getId() + " has been added to community with id " + community.getId());
        }
    }
    public Community getCommunityByName(String name){
        return communityRepository.findCommunityByName(name);
    }

    public Community findCommunityForUser(CommunitySearchModel model) throws Exception {
        Community community = getCommunityByName(model.getName());
        if(community.getMode() == CommunityMode.PRIVATE_MODE || community == null){
            throw new Exception("community: community with name "+ model.getName() +" not found.");
        } else {
            log.info("community: community with name " + model.getName() + " is found.");
            return community;
        }
    }
    public Community findCommunityForAdmin(CommunitySearchModel model) throws Exception {
        Community community = getCommunityByName(model.getName());
        if(community == null){
            throw new Exception("community: community with name "+ model.getName() +" not found.");
        } else {
            log.info("community: community with name " + model.getName() + " is found.");
            return community;
        }
    }

    public void delete(Long id) {
        log.info("community: attempt to delete community with id " + id + ".");
        communityRepository.deleteById(id);
        log.info("community: community with id " + id + " has been deleted.");
    }
}
