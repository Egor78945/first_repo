package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.BanDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanDetailsRepository extends JpaRepository<BanDetails, Long> {
    BanDetails findBanDetailsByUserId(Long id);
    BanDetails findBanDetailsById(Long id);
}
