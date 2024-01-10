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
        list.add("Vladivostok");
        list.add("Cheboksari");
        list.add("Kazan");
        list.add("Nizhniy-Novgorod");
        list.add("Novocheboksarsk");
        list.add("Kaliningrad");
        list.add("Sochi");
        list.add("Krasnodar");
        list.add("Ufa");
        list.add("Samara");
        list.add("Ekaterinburg");
    }
}
