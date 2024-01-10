package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewsModel {
    private String theme;
    private String description;
}
