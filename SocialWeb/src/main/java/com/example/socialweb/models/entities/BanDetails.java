package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table(name = "ban_details")
@Entity
public class BanDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "date")
    private String date;
    @Column(name = "reason")
    private String reason;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
    @OneToOne
    private User banned;
    public BanDetails(){
        date = new Date(System.currentTimeMillis()).toString();
    }
}