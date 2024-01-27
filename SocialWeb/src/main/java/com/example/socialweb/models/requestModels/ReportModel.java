package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.enums.ReportReason;
import lombok.Data;

@Data
public class ReportModel {
    private Long id;
    private ReportReason reason;
    private String message;
    public ReportModel(Long id){
        this.id = id;
    }
}
