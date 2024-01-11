package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.requestModels.CommunityModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> subscribers = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<News> news = new ArrayList<>();
    @Column(name = "mode")
    private CommunityMode mode = CommunityMode.PUBLIC_MODE;
    @ElementCollection
    private Map<User,ArrayList<String>> messages = new HashMap<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Report> listOfReports = new ArrayList<>();
}
