package com.example.socialweb.models.enums;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cities {
    List<String> list = new ArrayList<>();
    public Cities(){
        list.add("Moscow");
        list.add("Saint-Petersburg");
        list.add("Washington");
        list.add("New-York");
    }
}
