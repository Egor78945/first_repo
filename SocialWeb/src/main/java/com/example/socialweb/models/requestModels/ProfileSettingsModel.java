package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSettingsModel {
    private String name;
    private String surname;
    private String status;
    private String email;
    private Integer age;
    private String city;
    private String country;
    private boolean closeProfile;

    public ProfileSettingsModel build(User user) {
        return new ProfileSettingsModel(
                name = user.getName(),
                surname = user.getSurname(),
                status = user.getStatus(),
                email = user.getEmail(),
                age = user.getAge(),
                city = user.getCity(),
                country = user.getCountry(),
                closeProfile = user.getCloseProfile()
        );
    }
}
