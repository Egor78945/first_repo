package com.example.bank_spring_boot_mvc.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Transaction {
    private String email;
    private Long sum;
}
