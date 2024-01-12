package com.example.socialweb.services;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.repositories.CommunityRepository;
import com.example.socialweb.repositories.ReportRepository;
import com.example.socialweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public Community getCommunityById(Long id) {
        return communityRepository.findCommunityById(id);
    }

}
