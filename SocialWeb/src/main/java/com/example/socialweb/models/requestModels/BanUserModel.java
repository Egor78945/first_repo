package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class BanUserModel {
    private Long id;
    private String reason;

    public BanUserModel(Long id) {
        this.id = id;
    }
}
