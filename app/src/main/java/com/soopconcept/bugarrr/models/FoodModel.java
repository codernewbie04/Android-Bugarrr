package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class FoodModel {
    ArrayList<DetailFood> breakfast = new ArrayList<DetailFood>();
    ArrayList<DetailFood> dinner = new ArrayList<DetailFood>();
    ArrayList<DetailFood> drink = new ArrayList<DetailFood>();
    ArrayList<DetailFood> lunch = new ArrayList<DetailFood>();
    ArrayList<DetailFood> snack = new ArrayList<DetailFood>();

    public FoodModel(){}

    public FoodModel(ArrayList<DetailFood> breakfast, ArrayList<DetailFood> dinner, ArrayList<DetailFood> drink, ArrayList<DetailFood> lunch, ArrayList<DetailFood> snack)
    {
        this.breakfast = breakfast;
        this.dinner = dinner;
        this.drink = drink;
        this.lunch = lunch;
        this.snack = snack;
    }

    public ArrayList<DetailFood> getBreakfast() {
        return breakfast;
    }

    public ArrayList<DetailFood> getDinner() {
        return dinner;
    }

    public ArrayList<DetailFood> getDrink() {
        return drink;
    }

    public ArrayList<DetailFood> getLunch() {
        return lunch;
    }

    public ArrayList<DetailFood> getSnack() {
        return snack;
    }
}
