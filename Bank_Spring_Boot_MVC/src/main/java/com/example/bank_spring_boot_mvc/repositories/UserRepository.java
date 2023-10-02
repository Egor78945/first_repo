package com.example.bank_spring_boot_mvc.repositories;

import com.example.bank_spring_boot_mvc.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    boolean existsUserByEmail(String email);
}
