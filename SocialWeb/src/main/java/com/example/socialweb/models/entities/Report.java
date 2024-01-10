package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "theme")
    private String reason;
    @Column(name = "message")
    private String message;
    @OneToOne
    private User applicant;
    @OneToOne
    private User appealed;
    @OneToOne
    private News news;
    @OneToOne
    private Community community;
    @Column(name = "date")
    private String date;
}
