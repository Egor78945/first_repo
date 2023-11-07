package com.example.headhunter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Column(name = "date_of_registration")
    private String dateOfRegistration;
    @Column(name = "role")
    private Role role;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Vacancy> vacancyList;
}
