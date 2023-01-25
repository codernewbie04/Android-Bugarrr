package com.soopconcept.bugarrr.models;

public class Activity_target {
    private int calories;
    private int calories_intake;
    private int exercise;
    private int sleep;
    private int stand;

    public Activity_target(){}

    public Activity_target(int calories, int calories_intake, int exercise, int sleep, int stand)
    {
        this.calories = calories;
        this.calories_intake = calories_intake;
        this.exercise = exercise;
        this.sleep = sleep;
        this.stand = stand;
    }


    public int getCalories_intake() {
        return calories_intake;
    }

    public int getCalories() {
        return calories;
    }

    public int getExercise() {
        return exercise;
    }

    public int getSleep() {
        return sleep;
    }

    public int getStand() {
        return stand;
    }
}
