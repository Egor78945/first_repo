package com.example.socialweb.services;

import com.example.socialweb.models.entities.BanDetails;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.BanUserModel;
import com.example.socialweb.repositories.BanDetailsRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BanDetailsService {
    private final BanDetailsRepository banDetailsRepository;
    private final UserRepository userRepository;

    public BanDetails getBanDetailsByUserId(Long id) {
        return banDetailsRepository.findBanDetailsByUserId(id);
    }

    public BanDetails getBanDetailsById(Long id) {
        return banDetailsRepository.findBanDetailsById(id);
    }

    @Transactional
    public void unban(Long id) {
        log.info("admin: attempt to unban user...");
        BanDetails banDetails = getBanDetailsByUserId(id);
        User user = userRepository.findUserById(id);
        if (user.getIsBan()) {
            log.info("admin: attempt to unban user...");
            user.setIsBan(false);
            log.info("admin: attempt to delete ban details...");
            banDetailsRepository.delete(banDetails);
            log.info("admin: ban details has been deleted, saving the user...");
            userRepository.save(user);
            log.info("admin: user has been saved.");
        } else {
            log.info("admin: user is not banned.");
        }
    }

    @Transactional
    public void ban(User userToBan, User banned, BanUserModel model) {
        log.info("admin: Attempt to ban user...");
        if(!userToBan.getIsBan()){
            userToBan.setIsBan(true);
            log.info("admin: user has been banned, creating ban details...");
            BanDetails banDetails = new BanDetails();
            banDetails.setBanned(banned);
            banDetails.setUser(userToBan);
            banDetails.setReason(model.getReason());
            log.info("admin: user details created, saving ban details and user...");
            userRepository.save(userToBan);
            log.info("admin: user has been saved.");
            banDetailsRepository.save(banDetails);
            log.info("admin: ban details has been saved.");
        } else {
            log.info("admin: user is already banned.");
        }
    }
}
