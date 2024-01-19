package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.Comment;
import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.NewsTheme;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.List;

@Data
public class PostNewsModel {
    private String description;
    private NewsTheme newsTheme;
}
