package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class ChangePassword {
    private String newPassword;
    private String confirmPassword;
}
