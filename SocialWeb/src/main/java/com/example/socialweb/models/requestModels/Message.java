package com.example.socialweb.models.requestModels;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message {
    Long id;
    String message;

    public Message(Long id) {
        this.id = id;
    }

}
