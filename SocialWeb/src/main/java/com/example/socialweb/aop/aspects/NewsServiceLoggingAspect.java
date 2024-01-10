package com.example.socialweb.aop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class NewsServiceLoggingAspect {
    @Before("execution(public void publicNews(com.example.socialweb.models.requestModels.CreateNewsModel, com.example.socialweb.models.entities.User))")
    public void beforePublicNews(){
        log.info("news: attempt to public news...");
    }
    @After("execution(public void publicNews(com.example.socialweb.models.requestModels.CreateNewsModel, com.example.socialweb.models.entities.User))")
    public void afterPublicNews(){
        log.info("news: news has been public.");
    }
    @Before("execution(public void like(java.lang.Long, java.lang.String))")
    public void beforeLikeNews(){
        log.info("news: attempt to like a news...");
    }
    @Before("execution(public void comment(com.example.socialweb.models.requestModels.CommentModel, java.lang.String))")
    public void beforeCommentNews(){
        log.info("news: attempt to comment the news...");
    }
}
