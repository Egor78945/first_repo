package com.example.socialweb.models.requestModels;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class BanUserBody {
    private String reason;
}
