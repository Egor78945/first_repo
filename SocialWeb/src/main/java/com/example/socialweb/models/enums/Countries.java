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
        list.add("France");
        list.add("Germany");
        list.add("Spain");
        list.add("Poland");
        list.add("USA");
        list.add("Kazakhstan");
        list.add("Sweden");
        list.add("GB");
        list.add("Brazil");
        list.add("Australia");
        list.add("UAE");
    }
}
