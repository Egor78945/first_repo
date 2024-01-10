package com.example.socialweb.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@Slf4j
public class CommunityServiceLoggingAspect {
    @Before("execution(public boolean userCanCreateCommunity(com.example.socialweb.models.entities.User))")
    public void beforeCheckUserForCreateCommunity(){
        log.info("community: start to checking user community count...");
    }
    @Before("execution(public void createCommunity(com.example.socialweb.models.requestModels.CommunityModel, com.example.socialweb.models.entities.User))")
    public void beforeCreatingCommunity(){
        log.info("community: Start to creating community...");
    }
    @Before("execution(public boolean checkMessage(java.lang.String))")
    public void beforeCheckMessage(){
        log.info("community: start to check message.");
    }
    @Before("execution(public void userMessage(com.example.socialweb.models.requestModels.Message, com.example.socialweb.models.entities.User, com.example.socialweb.models.entities.Community))")
    public void beforeUserMessage(){
        log.info("community: attempt to send message.");
    }
    @Before("execution(public void subscribe(java.lang.Long, com.example.socialweb.models.entities.User))")
    public void beforeSubscribe(){
        log.info("community: attempt to subscribe...");
    }
    @Before("execution(public void invite(java.lang.Long, java.lang.Long))")
    public void beforeInviteMessage(){
        log.info("community: attempt to send invite message...");
    }
}
