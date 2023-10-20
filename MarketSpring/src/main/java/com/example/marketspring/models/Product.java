package com.example.marketspring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "description")
    private String description;
    @Column(name = "date_of_publication")
    private String dateOfPublication;
    @Column(name = "user_id")
    private Long ownerId;

    public Product(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }
}
