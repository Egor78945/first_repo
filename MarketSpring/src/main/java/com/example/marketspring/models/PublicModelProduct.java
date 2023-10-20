package com.example.marketspring.models;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicModelProduct {
    private String name;
    private int price;
    private String description;
}
