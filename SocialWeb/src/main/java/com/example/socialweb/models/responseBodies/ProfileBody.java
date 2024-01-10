package com.example.socialweb.models.responseBodies;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileBody {
    public ProfileBody build(User user) {
        return new ProfileBody(
                this.id = user.getId(),
                this.name = user.getName(),
                this.surname = user.getSurname(),
                this.status = user.getStatus(),
                this.email = user.getEmail(),
                this.age = user.getAge(),
                this.city = user.getCity(),
                this.country = user.getCountry(),
                this.role = user.getRole(),
                this.friendCount = user.getFriendList().size(),
                this.messageCount = user.getMessageList().size()
        );
    }

    private Long id;
    private String name;
    private String surname;
    private String status;
    private String email;
    private Integer age;
    private String city;
    private String country;
    private Role role;
    private int friendCount;
    private int messageCount;
}
