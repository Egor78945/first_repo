package com.example.socialweb.services;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.Report;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.ReportModel;
import com.example.socialweb.repositories.CommunityRepository;
import com.example.socialweb.repositories.NewsRepository;
import com.example.socialweb.repositories.ReportRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public void reportNews(User applicant, News news, ReportModel model) {
        log.info("report: attempt to report news " + news.getId() + ", reason - " + model.getReason() + ".");
        Report report = new Report();
        report.setNews(news);
        report.setDate(new Date(System.currentTimeMillis()).toString());
        report.setApplicant(applicant);
        report.setReason(model.getReason());
        report.setMessage(model.getMessage());
        news.getListOfReports().add(report);
        reportRepository.save(report);
        newsRepository.save(news);
        log.info("report: news " + news.getId() + " has been reported.");
    }
    public List<Report> getAllReports(){
        return reportRepository.findAll();
    }

    public void reportUser(ReportModel model, Long id, User user) throws Exception {
        if(model.getReason().length() < 4 || model.getMessage().length() > 150){
            throw new Exception("report: invalid report data.");
        } else {
            User appealed = userRepository.findUserById(id);
            Report report = new Report();
            report.setAppealed(appealed);
            report.setDate(new Date(System.currentTimeMillis()).toString());
            report.setReason(model.getReason());
            report.setApplicant(user);
            report.setMessage(model.getMessage());
            reportRepository.save(report);
            appealed.getListOfReports().add(report);
            userRepository.save(appealed);
        }
    }
    public void reportCommunity(Community community, ReportModel model, User user) throws Exception {
        if(model.getReason().length() < 4 || model.getReason().length() > 150) {
            throw new Exception("report: report reason is invalid.");
        } else {
            Report report = new Report();
            report.setMessage(model.getMessage());
            report.setReason(model.getReason());
            report.setApplicant(user);
            report.setDate(new Date(System.currentTimeMillis()).toString());
            report.setCommunity(community);
            reportRepository.save(report);
            community.getListOfReports().add(report);
            communityRepository.save(community);
            log.info("report: user with id " + user.getId() + " reported community with id " + community.getId() + ".");
        }
    }
}
