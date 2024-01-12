package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Table(name = "comments")
@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    private News news;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> commentator;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> likes;
    @Column(name = "date")
    private String date;
    public Comment(){
        likes = new ArrayList<>();
        date = new Date(System.currentTimeMillis()).toString();
    }
}
