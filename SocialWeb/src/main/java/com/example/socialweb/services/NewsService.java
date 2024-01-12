package com.example.socialweb.services;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
