package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.ReportReason;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "theme")
    private ReportReason reason;
    @Column(name = "message")
    private String message;
    @ManyToOne(cascade = CascadeType.ALL)
    private User applicant;
    @ManyToOne(cascade = CascadeType.ALL)
    private User appealed;
    @ManyToOne(cascade = CascadeType.ALL)
    private News news;
    @OneToOne(cascade = CascadeType.ALL)
    private Community community;
    @Column(name = "date")
    private String date;
    public Report(){
        date = new Date(System.currentTimeMillis()).toString();
    }
}
