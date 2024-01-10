package com.example.socialweb.services;

import com.example.socialweb.errors.AttributeError;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final UserService userService;

    public boolean checkUserForBan(User user){
        if(user.getRole() == Role.USER_ROLE ){
            if(user.getIsBan()){
               log.info("User with id " + user.getId() + " already is banned.");
               return false;
            } else {
                return true;
            }
        } else {
            log.info("admin: You have not access to ban user with id " + user.getId() + ".");
            return false;
        }
    }
    public boolean checkUserForUnban(User user){
        if(!user.getIsBan()){
            log.info("admin: user with id " + user.getId() + " is not banned.");
            return false;
        }
        return true;
    }
    public void banUser(User user, User admin, String reason) {
        if(checkUserForBan(user)) {
                user.setIsBan(true);
                user.getBanHistory().put(new Date(System.currentTimeMillis()).toString(), admin.getId() + "(" + admin.getName() + " " + admin.getSurname() + "):" + reason + ".");
                userService.saveUser(user);
                log.info("admin: user " + user.getId() + " has been banned.");
        }
    }

    public void unbanUser(User user, User admin) {
        if(checkUserForUnban(user)) {
            user.setIsBan(false);
            userService.saveUser(user);
            log.info("admin: admin with id " + admin.getId() + " unbanned user with id " + user.getId() + ".");
        }
    }
}
