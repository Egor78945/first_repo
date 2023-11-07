package com.example.headhunter.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name ="vacancies")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "experience")
    private String experience;
    @Column(name = "description")
    private String description;
    @Column(name = "work_type")
    private WorkType workType;
    @Column(name = "work_format")
    private WorkFormat workFormat;
    @Column(name = "date_of_registration")
    private String dateOfPublication;
    @Column(name = "user_id")
    private Long userId;
}
