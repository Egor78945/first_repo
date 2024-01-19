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
    @ManyToOne(cascade = CascadeType.ALL)
    private News news;
    @Column(name = "comment")
    private String comment;
    @Column(name = "date")
    private String date;
    public Comment(){
        date = new Date(System.currentTimeMillis()).toString();
    }
}
