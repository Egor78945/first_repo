package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.NewsTheme;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.convert.DataSizeUnit;

import java.util.*;

@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Data
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    private String date;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> like;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User publisher;
    @Column(name = "theme")
    private NewsTheme newsTheme;
    public News(){
        comments = new ArrayList<>();
        like = new ArrayList<>();
        date = new Date(System.currentTimeMillis()).toString();
    }
    public void comment(Comment comment){
        comments.add(comment);
    }
}