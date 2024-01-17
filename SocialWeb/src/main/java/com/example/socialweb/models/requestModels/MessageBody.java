package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class MessageBody {
    private Long id;
    private String message;
    public MessageBody(Long id){
        this.id = id;
    }
}
