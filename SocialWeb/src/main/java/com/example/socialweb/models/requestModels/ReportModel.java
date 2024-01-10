package com.example.socialweb.models.requestModels;

import com.example.socialweb.models.entities.News;
import com.example.socialweb.models.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportModel {
    private Long id;
    private String reason;
    private String message;
    public ReportModel(Long id){
        this.id = id;
    }
}
