package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.Community;
import com.example.socialweb.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findAllByOwner(User user);
    Community findCommunityById(Long id);
}
