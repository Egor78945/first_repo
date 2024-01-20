package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class CommentNewsModel {
    private Long id;
    private String comment;
    public CommentNewsModel(Long id){
        this.id = id;
    }
}
