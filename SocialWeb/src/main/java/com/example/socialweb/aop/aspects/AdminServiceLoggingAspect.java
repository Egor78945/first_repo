package com.example.socialweb.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
@Slf4j
public class AdminServiceLoggingAspect {
    @Before("execution(public void banUser(com.example.socialweb.models.entities.User, com.example.socialweb.models.entities.User, java.lang.String))")
    public void beforeBanUserAttempt(){
        log.info("admin: attempt to ban user...");
    }
    @Before("execution(public void unbanUser(com.example.socialweb.models.entities.User, com.example.socialweb.models.entities.User))")
    public void beforeUnbanUserAttempt(){
        log.info("admin: attempt to unban user...");
    }
}
