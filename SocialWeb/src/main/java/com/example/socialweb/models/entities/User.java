package com.example.socialweb.models.entities;


import com.example.socialweb.models.enums.Role;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "status")
    private String status;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Community> communities;
    @Column(name = "age")
    private Integer age;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<News> news = new ArrayList<>();
    @Column(name = "city")
    private String city;
    @Column(name = "role")
    private Role role;
    @Column(name = "country")
    private String country;
    @Column(name = "password")
    private String password;
    @Column(name = "registerDate")
    private String registerDate;
    @ElementCollection
    private Map<User, ArrayList<String>> messageList = new HashMap<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> friendList = new ArrayList<>();
    @ElementCollection
    private Map<String, String> banHistory = new HashMap<>();
    @ElementCollection
    private Map<String, String> lockHistory = new HashMap<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Report> listOfReports = new ArrayList<>();
    @Column(name = "isBan")
    private Boolean isBan;
    @Column(name = "isLock")
    private Boolean isLock;
    @Column(name = "isClose")
    private Boolean closeProfile;
}
