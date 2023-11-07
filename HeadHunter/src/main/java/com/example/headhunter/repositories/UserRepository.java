package com.example.headhunter.repositories;

import com.example.headhunter.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByPhoneNumber(String phoneNumber);
}
