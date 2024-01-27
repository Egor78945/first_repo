package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findReportById(Long id);
    List<Report> findAllByAppealedIsNotNull();
}
