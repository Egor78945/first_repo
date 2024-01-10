package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    List<User> findAllByName(String name);
    List<User> findAllBySurname(String surname);
    List<User> findAllByNameAndSurname(String name, String surname);
    User findUserById(Long id);
    Boolean existsUserByEmail(String email);
    List<User> findUserByIsBanTrue();
    List<User> findAllByListOfReportsIsNotEmpty();
}
