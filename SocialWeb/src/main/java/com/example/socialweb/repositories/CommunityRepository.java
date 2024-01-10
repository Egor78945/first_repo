package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.CommunityMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Community findCommunityById(Long id);
    List<Community> findAllByOwner(User user);
    List<Community> findAllByMode(CommunityMode mode);
    Community findCommunityByName(String name);
}
