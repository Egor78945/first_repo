package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.CommunityMode;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CommunityModel {
    private String name;
    private String description;
    private CommunityMode mode = CommunityMode.PUBLIC_MODE;
}
