package com.example.socialweb.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "theme")
    private String reason;
    @Column(name = "message")
    private String message;
    @OneToOne(cascade = CascadeType.ALL)
    private User applicant;
    @OneToOne(cascade = CascadeType.ALL)
    private User appealed;
    @OneToOne(cascade = CascadeType.ALL)
    private News news;
    @OneToOne(cascade = CascadeType.ALL)
    private Community community;
    @Column(name = "date")
    private String date;
}
