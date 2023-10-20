package com.example.marketspring.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class RegBody {
    private String email;
    private String name;
    private String surname;
    private String password;
}
