package com.example.socialweb.models.requestModels;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UpdateDataModel {
    private String name;
    private String surname;
    private String status;
    private String email;
    private Integer age;
    private String city;
    private String country;
    private String password;
    private Boolean closeProfile;
}
