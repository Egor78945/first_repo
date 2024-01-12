package com.example.socialweb.services;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.Report;
import com.example.socialweb.repositories.CommunityRepository;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.ReportRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    public Report getReportById(Long id){
        return reportRepository.findReportById(id);
    }
    public Report getReportByNews(News news){
        return reportRepository.findReportByNews(news);
    }


}
