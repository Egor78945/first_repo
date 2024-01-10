package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.User;
import com.example.socialweb.models.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBody {
    private String name;
    private String surname;
    @Size(min = 1, max = 20, message = "Your status to long or to short, it should to be less 21 and greater 0.")
    @NotEmpty
    private String status;
    private String email;
    @Range(min = 14, message = "Minimum age for our website - 14.")
    private Integer age;
    private String city;
    private String country;
    private String password;
    public RegisterBody build(User user){
        return new RegisterBody(
                this.name = user.getName(),
                this.surname = user.getSurname(),
                this.status = user.getStatus(),
                this.email = user.getEmail(),
                this.age = user.getAge(),
                this.city = user.getCity(),
                this.country = user.getCountry(),
                this.password = user.getPassword()
        );
    }
}
