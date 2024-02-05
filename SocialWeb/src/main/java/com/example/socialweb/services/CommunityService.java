package com.example.socialweb.services;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.CommunityModel;
import com.example.socialweb.repositories.CommunityRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public List<Community> getAllCommunitiesByOwner(User user){
        return communityRepository.findAllByOwner(user);
    }

    private boolean isValidCommunityData(CommunityModel model, User user, List<Community> userCommunities) {
        log.info("community: checking community data...");
        if (!isValidName(model.getName()) || !isValidDescription(model.getDescription()) || !userCanCreateCommunity(userCommunities)) {
            log.info("community: community date is invalid.");
            return false;
        }
        log.info("community: community data is valid.");
        return true;
    }

    private boolean isValidName(String name) {
        log.info("community: checking name...");
        if (name.length() < 3 || name.length() > 30) {
            log.info("community: name is too long or too short.");
            return false;
        }
        log.info("community: name is valid.");
        return true;
    }

    private boolean isValidDescription(String description) {
        log.info("community: checking description...");
        if (description.length() > 50) {
            log.info("community: description is too long.");
            return false;
        }
        log.info("community: description is valid.");
        return true;
    }

    private boolean userCanCreateCommunity(List<Community> userComms) {
        log.info("community: checking user...");
        if (userComms.size() >= 3) {
            log.info("community: user is already have 3 communities.");
            return false;
        }
        log.info("community: user can create community.");
        return true;
    }

    @Transactional
    public boolean createCommunity(CommunityModel communityModel, User currentUser, List<Community> userCommunities) {
        log.info("community: attempt to create the community...");
        if (isValidCommunityData(communityModel, currentUser, userCommunities)) {
            Community community = new Community();
            community.setName(communityModel.getName());
            community.setDescription(communityModel.getDescription());
            community.setOwner(currentUser);
            community.setMode(communityModel.getMode());
            community.setTheme(communityModel.getTheme());
            community.getSubscribers().add(currentUser);
            currentUser.getCommunities().add(community);
            log.info("community: attempt to save the community and user...");
            communityRepository.save(community);
            log.info("community: community has been saved.");
            userRepository.save(currentUser);
            log.info("community: user has been saved.");
            return true;
        } else
            log.info("community: community is not created.");
        return false;
    }
    public Community getCommunityById(Long id){
        return communityRepository.findCommunityById(id);
    }

    @Transactional
    public boolean subscribe(Community community, User user) {
        log.info("community: attempt to subscribe user to community...");
        if(!community.getSubscribers().contains(user) && !user.getCommunities().contains(community)){
            user.getCommunities().add(community);
            community.getSubscribers().add(user);
            log.info("community: attempt to save the user and community...");
            userRepository.save(user);
            log.info("community: user has been saved.");
            communityRepository.save(community);
            log.info("community: community has been saved.");
            return true;
        }
        log.info("community: this user is already subscribed on this community.");
        return false;
    }

    @Transactional
    public boolean unsubscribe(Community community, User user) {
        log.info("community: attempt to unsubscribe user from community...");
        if(community.getSubscribers().contains(user) && user.getCommunities().contains(community)){
            user.getCommunities().remove(community);
            community.getSubscribers().remove(user);
            log.info("community: attempt to save the user and community...");
            userRepository.save(user);
            log.info("community: user has been saved.");
            communityRepository.save(community);
            log.info("community: community has been saved.");
            return true;
        }
        log.info("community: this user is not subscribed on this community.");
        return false;
    }
    @Transactional
    public List<Community> getAllCommunities(){
        return communityRepository.findAll();
    }
}
