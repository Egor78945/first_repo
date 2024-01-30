package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.CommunityMode;
import com.example.socialweb.models.enums.CommunityTheme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
public class CommunityModel {
    private String name;
    private String description;
    private User owner;
    private CommunityMode mode;
    private CommunityTheme theme;
    public CommunityModel(){
        mode = CommunityMode.PUBLIC_MODE;
        theme = CommunityTheme.OTHER_THEME;
    }
}
