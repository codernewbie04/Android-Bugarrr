package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

public class LatihanModel {
    private int calories_burned;
    private Timestamp end_time;
    private Timestamp start_time;
    private String workout_name;

    public LatihanModel(){}

    public LatihanModel(int calories_burned, Timestamp end, Timestamp start, String workout_name)
    {
        this.calories_burned = calories_burned;
        this.end_time = end;
        this.start_time = start;
        this.workout_name = workout_name;
    }

    public int getCalories_burned() {
        return calories_burned;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setCalories_burned(int calories_burned) {
        this.calories_burned = calories_burned;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }
}
