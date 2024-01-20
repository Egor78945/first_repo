package com.example.socialweb.services;

import com.example.socialweb.models.entities.Comment;
import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.CommentNewsModel;
import com.example.socialweb.repositories.CommentRepository;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;

    public Comment getCommentById(Long id) {
        return commentRepository.findCommentById(id);
    }

    public void comment(CommentNewsModel model, News newsById, User currentUser) {
        log.info("comment: attempt to comment the news...");
        if (model.getComment().length() < 100 && !model.getComment().isEmpty()) {
            log.info("comment: comment is valid, creating the comment...");
            Comment comment = new Comment();
            comment.setComment(model.getComment());
            comment.setNews(newsById);
            comment.setCommentator(currentUser);
            newsById.comment(comment);
            log.info("comment: comment is created, saving...");
            commentRepository.save(comment);
            newsRepository.save(newsById);
            log.info("comment: comment has been saved.");
        } else {
            log.info("comment: comment is invalid.");
        }
    }

    public void deleteComment(Comment commentById, User user) {
        log.info("comment: attempt to delete the comment.");
        if (commentById.getCommentator().getId().equals(user.getId())) {

            News news = commentById.getNews();
            news.getComments().remove(commentById);
            log.info("comment: comment has been deleted, saving the news.");
            newsRepository.save(news);
            log.info("comment: news has been saved.");
        } else {
            log.info("comment: user cant delete other user comment.");
        }
    }
}
