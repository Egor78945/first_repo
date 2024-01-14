package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class PasswordSettingsModel {
    private String oldPassword;
    private String newPassword;
}
