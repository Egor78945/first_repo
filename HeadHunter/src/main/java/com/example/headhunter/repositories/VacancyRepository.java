package com.example.headhunter.repositories;

import com.example.headhunter.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    Vacancy findVacancyByName(String name);
}
