package com.example.socialweb.services;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.NewsTheme;
import com.example.socialweb.models.enums.Role;
import com.example.socialweb.models.requestModels.PostNewsModel;
import com.example.socialweb.repositories.CommentRepository;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getNewsById(Long id) {
        return newsRepository.findNewsById(id);
    }

    public List<News> getAllByPublisherId(Long id) {
        return newsRepository.findAllByPublisherId(id);
    }

    @Transactional
    public void saveNews(News news) {
        log.info("news: attempt to save the news...");
        newsRepository.save(news);
        log.info("news: news has been saved and published.");
    }

    @Transactional
    public void postNews(PostNewsModel model, User currentUser) {
        if (isValidNews(model)) {
            log.info("news: creating the news...");
            News news = new News();
            news.setNewsTheme(model.getNewsTheme());
            news.setDescription(model.getDescription());
            news.setPublisher(currentUser);
            log.info("news: news has been creating, saving...");
            saveNews(news);
        } else {
            log.info("news: news is not saved.");
        }
    }

    private boolean isValidNews(PostNewsModel model) {
        log.info("news: checking news...");
        if (model.getNewsTheme() != null) {
            log.info("news: theme is valid.");
            if (model.getDescription().length() < 100 && !model.getDescription().isEmpty()) {
                log.info("news: description is valid.");
                return true;
            } else {
                log.info("news: description is invalid.");
                return false;
            }
        } else {
            log.info("news: theme is invalid.");
            return false;
        }

    }

    @Transactional
    public void like(News newsById, User user) {
        log.info("news: attempt to like news...");
        if (!newsById.getLike().contains(user)) {
            user.like(newsById);
            log.info("news: news liked, saving the news...");
            saveNews(newsById);
        } else {
            log.info("news: user is already liked this news.");
        }
    }

    @Transactional
    public void unlike(News newsById, User user) {
        log.info("news: attempt to unlike news...");
        if (newsById.getLike().contains(user)) {
            user.unlike(newsById);
            log.info("news: news unliked, saving the news...");
            saveNews(newsById);
        } else {
            log.info("news: user is not liked the news.");
        }
    }

    @Transactional
    public void deleteNews(News newsById, User user) {
        log.info("news: attempt to delete the news...");
        if (newsById.getPublisher().getId().equals(user.getId()) || user.isAdmin()) {
            log.info("news: attempt to delete all comments by news...");
            commentRepository.deleteAllByNewsId(newsById.getId());
            log.info("news: all comments by news " + newsById.getId() + " has been deleted.");
            newsRepository.delete(newsById);
            log.info("news: news has been deleted.");
        } else {
            log.info("news: User cant delete other user news.");
        }
    }
}
