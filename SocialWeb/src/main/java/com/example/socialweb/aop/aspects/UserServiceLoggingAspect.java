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
        log.info("register: attempt to save the user...");
    }

    @After("execution(public void com.example.socialweb.services.UserService.register(com.example.socialweb.models.requestModels.RegisterBody, org.springframework.security.crypto.password.PasswordEncoder))")
    public void afterRegisterAdvice() {
        log.info("register: the user successfully saved.");
    }

    @Before("execution(public void addErrorsToBindingResultForRegister(org.springframework.validation.BindingResult,com.example.socialweb.models.requestModels.RegisterBody))")
    public void beforeCheckRegisterAdvice() {
        log.info("register: start checking user data...");
    }

    @After("execution(public void addErrorsToBindingResultForRegister(org.springframework.validation.BindingResult,com.example.socialweb.models.requestModels.RegisterBody))")
    public void afterCheckRegisterAdvice() {
        log.info("register: user data is valid.");
    }

    @Before("execution(public void addErrorsToBindingResultForUpdate(org.springframework.validation.BindingResult,com.example.socialweb.models.entities.User))")
    public void beforeCheckUpdateAdvice() {
        log.info("update: start checking user data...");
    }

    @After("execution(public void addErrorsToBindingResultForUpdate(org.springframework.validation.BindingResult,com.example.socialweb.models.entities.User))")
    public void afterCheckUpdateAdvice() {
        log.info("update: user data is valid...");
    }

    @Before("execution(public void updateUser(com.example.socialweb.models.entities.User,org.springframework.security.crypto.password.PasswordEncoder,com.example.socialweb.models.entities.User))")
    public void beforeUpdateUserAdvice() {
        log.info("update: attempt to update the user.");
    }

    @After("execution(public void updateUser(com.example.socialweb.models.entities.User,org.springframework.security.crypto.password.PasswordEncoder,com.example.socialweb.models.entities.User))")
    public void afterUpdateUserAdvice() {
        log.info("update: user have been updated.");
    }

    @Before("execution(public void changeUserPassword(java.lang.String,java.lang.String, org.springframework.security.crypto.password.PasswordEncoder))")
    public void beforeChangePassword() {
        log.info("update(password): attempt to update a password.");
    }

    @After("execution(public void changeUserPassword(java.lang.String,java.lang.String, org.springframework.security.crypto.password.PasswordEncoder))")
    public void afterChangePassword() {
        log.info("update(password): password has been updated.");
    }

}
