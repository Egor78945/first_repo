package com.example.socialweb.models.requestModels;

import lombok.Data;

@Data
public class CommentModel {
    private Long id;
    private String comment;

    public CommentModel(Long id) {
        this.id = id;
    }
}
