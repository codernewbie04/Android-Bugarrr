package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

public class DetailFood {
    private Timestamp date_added;
    private int food_calories;
    private String food_name;
    private int food_weight;

    public DetailFood(){}

    public DetailFood(Timestamp date, int calories, String name, int weight)
    {
        this.date_added = date;
        this.food_calories = calories;
        this.food_name = name;
        this.food_weight = weight;
    }

    public int getFood_calories() {
        return food_calories;
    }

    public int getFood_weight() {
        return food_weight;
    }

    public String getFood_name() {
        return food_name;
    }

    public Timestamp getDate_added() {
        return date_added;
    }
}
