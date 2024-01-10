package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "politic")
    private String theme;
    private String publicationDate;
    @OneToMany
    private List<User> like = new ArrayList<>();
    @ElementCollection
    private Map<User, String> comments = new HashMap<>();
    private String description;
    @Column(name = "user_id")
    private Long publisherId;
    @OneToMany
    private List<Report> listOfReports = new ArrayList<>();
}