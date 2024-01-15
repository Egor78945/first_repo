package com.example.socialweb.repositories;

import com.example.socialweb.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserById(Long id);

    List<User> findUserByIsBanTrue();

    Boolean existsUserByEmail(String email);

    List<User> findAllByNameAndSurname(String name, String surname);

    List<User> findAllBySurname(String surname);

    List<User> findAllByName(String name);

    Boolean existsUserByNameAndSurname(String name, String surname);
    Boolean existsUserBySurname(String surname);
    Boolean existsUserByName(String name);

}
