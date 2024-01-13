package com.example.socialweb.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class UserServiceLoggingAspect {
    @Before("execution(public void com.example.socialweb.services.UserService.register(com.example.socialweb.models.requestModels.RegisterBody, org.springframework.security.crypto.password.PasswordEncoder))")
    public void beforeRegisterAdvice() {
        log.info("register: attempt to register the user...");
    }

    @After("execution(public void com.example.socialweb.services.UserService.register(com.example.socialweb.models.requestModels.RegisterBody, org.springframework.security.crypto.password.PasswordEncoder))")
    public void afterRegisterAdvice() {
        log.info("register: the user successfully registered.");
    }
}
