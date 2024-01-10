package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.requestModels.CommunityModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "community")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    private String description;
    @ManyToOne
    private User owner;
    @OneToMany
    private List<User> subscribers = new ArrayList<>();
    @OneToMany
    private List<News> news = new ArrayList<>();
    @Column(name = "mode")
    private CommunityMode mode = CommunityMode.PUBLIC_MODE;
    @ElementCollection
    private Map<User,ArrayList<String>> messages = new HashMap<>();
    @OneToMany
    private List<Report> listOfReports = new ArrayList<>();
}
