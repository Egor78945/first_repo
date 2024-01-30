package com.example.socialweb.models.entities;

import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.enums.CommunityTheme;
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
    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> subscribers;
    @Column(name = "mode")
    private CommunityMode mode;
    @Column(name = "theme")
    private CommunityTheme theme;
    public Community(){
        subscribers = new ArrayList<>();
        mode = CommunityMode.PUBLIC_MODE;
        theme = CommunityTheme.OTHER_THEME;
    }
}
