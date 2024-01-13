package com.example.socialweb.models.enums;

import jakarta.annotation.sql.DataSourceDefinitions;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Countries {
    List<String> list = new ArrayList<>();
    public Countries(){
        list.add("Russia");
        list.add("USA");
    }
}
