package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "message")
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private User sender;
    @OneToOne(cascade = CascadeType.ALL)
    private User recipient;
    @Column(name = "message")
    private String message;
    @Column(name = "date")
    private String date;
    public Message(){
        date = new Date(System.currentTimeMillis()).toString();
    }
}
