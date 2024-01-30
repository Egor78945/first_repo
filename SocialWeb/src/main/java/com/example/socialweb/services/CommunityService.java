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
        if (name.length() < 3 || name.length() > 20) {
            log.info("community: name is too long or too short.");
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isDigit(name.charAt(i)) && !Character.isLetter(name.charAt(i)) && name.charAt(i) != ' ') {
                log.info("community: name contains invalid symbols.");
                return false;
            }
        }
        log.info("community: name is valid.");
        return true;
    }

    private boolean isValidDescription(String description) {
        log.info("community: checking description...");
        if (description.length() > 30) {
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
            log.info("community: attempt to save the community...");
            communityRepository.save(community);
            log.info("community: community has been saved.");
            return true;
        } else
            log.info("community: community is not created.");
        return false;
    }

}
