package com.example.marketspring.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProfileModel {
    private Long id;
    private String name;
    private String surname;
    private Long balance;
    private String dateOfRegister;
    private List<Product> userProducts;
}
