package com.example.socialweb.services;

import com.example.socialweb.errors.AttributeError;
import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.CommentModel;
import com.example.socialweb.models.requestModels.CreateNewsModel;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public News getNewsById(Long id){
        return newsRepository.findNewsById(id);
    }
    public List<News> getAllNewsByPublisherId(Long id){
        return newsRepository.findAllByPublisherId(id);
    }
    public void publicNews(CreateNewsModel model, User user){
        News news = new News();
        news.setDescription(model.getDescription());
        news.setTheme(model.getTheme());
        news.setPublicationDate(new Date(System.currentTimeMillis()).toString());
        user.getNews().add(news);
        newsRepository.save(news);
        userRepository.save(user);
    }
    public void like(Long id, String email){
        News news = getNewsById(id);
        User user = userService.getUserByEmail(email);
        if(!news.getLike().contains(user)) {
            news.getLike().add(user);
            log.info("news: user " + user.getId() + " liked news " + id + ".");
        } else {
            news.getLike().remove(user);
            log.info("news: user " + user.getId() + " removed the like from the news " + id + ".");
        }
        newsRepository.save(news);
    }
    public List<News> getAllNews(){
        return newsRepository.findAll();
    }

    public void comment(CommentModel model, String name) {
        News news = getNewsById(model.getId());
        User user = userService.getUserByEmail(name);
        news.getComments().put(user, model.getComment());
        newsRepository.save(news);
        log.info("news: user "+user.getId()+" liked news "+news.getId()+".");
    }
    public void checkCommentValid(BindingResult bindingResult, CommentModel model){
        if(model.getComment().isEmpty()){
            bindingResult.addError(new AttributeError("CommentModel","comment", "Comment could not to be empty."));
        }
        if(model.getComment().length() > 150){
            bindingResult.addError(new AttributeError("CommentModel","comment","Comment is too long. Maximum size is 150 symbols."));
        }
    }
    public List<News> getAllReportNews(){
        return newsRepository.findAllByListOfReportsIsNotEmpty();
    }
    public void deleteNews(Long id){
        newsRepository.deleteById(id);
    }
}
