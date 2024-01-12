package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.CommunityMode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    private User owner;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> subscribers;
    @Column(name = "mode")
    private CommunityMode mode;
    public Community(){
        subscribers = new ArrayList<>();
    }
}
