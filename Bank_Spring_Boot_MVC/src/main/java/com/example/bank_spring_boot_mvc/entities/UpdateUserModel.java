package com.example.bank_spring_boot_mvc.entities;

import lombok.Data;

@Data
public class UpdateUserModel {
    private String name;
    private String email;
    private String password;
}
