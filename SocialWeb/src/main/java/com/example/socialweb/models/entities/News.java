package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "politic")
    private String theme;
    @Column(name = "date")
    private String date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> like;
    @Column(name = "description")
    private String description;
    @OneToOne
    private User publisher;
    public News(){
        like = new ArrayList<>();
        date = new Date(System.currentTimeMillis()).toString();
    }
}