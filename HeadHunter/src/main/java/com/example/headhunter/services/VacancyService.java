package com.example.headhunter.services;

import com.example.headhunter.repositories.VacancyRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VacancyService {
    private final VacancyRepository vacancyRepository;
}
