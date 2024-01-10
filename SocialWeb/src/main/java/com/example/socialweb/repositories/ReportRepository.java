package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    Report findReportById(Long id);
    Report findReportByNews(News news);

}
