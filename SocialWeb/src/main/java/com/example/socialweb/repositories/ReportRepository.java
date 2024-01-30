package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report findReportById(Long id);
    List<Report> findAllByAppealedIsNotNull();
}
