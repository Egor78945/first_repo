package com.example.socialweb.services;

import com.example.socialweb.models.entities.Report;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.requestModels.ReportModel;
import com.example.socialweb.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public void reportUser(User userToReport, ReportModel reportModel, User applicant) {
        log.info("report: attempt to report the user...");
        if (userToReport.getId().equals(applicant.getId()))
            log.info("report: user cant report himself.");
        else if (reportModel.getReason() == null || reportModel.getMessage().isEmpty() || reportModel.getMessage().length() > 150)
            log.info("report: reason or message is invalid.");
        else {
             Report report = new Report();
             report.setApplicant(applicant);
             report.setAppealed(userToReport);
             report.setReason(reportModel.getReason());
             report.setMessage(reportModel.getMessage());
             reportRepository.save(report);
             log.info("report: user has been reported.");
        }

    }
    public List<Report> getAllReportedUsers(){
        return reportRepository.findAllByAppealedIsNotNull();
    }
}
