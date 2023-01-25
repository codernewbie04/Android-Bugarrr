package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class AktivitasModel {
    private int calories;
    private int exercise;
    Sleep SleepObject;
    ArrayList<Timestamp> stand_data = new ArrayList<Timestamp>();
    private int stands;

    public AktivitasModel(){}

    public AktivitasModel(int cal, int exer, Sleep sleep, ArrayList<Timestamp> standdata, int stands) {
        this.calories = cal;
        this.exercise = exer;
        this.SleepObject = sleep;
        this.stand_data = standdata;
        this.stands = stands;
    }
    // Getter Methods

    public int getCalories() {
        return calories;
    }

    public int getExercise() {
        return exercise;
    }

    public Sleep getSleep() {
        return SleepObject;
    }

    public ArrayList<Timestamp> getStand_data() {
        return stand_data;
    }

    public int getStands() {
        return stands;
    }

    // Setter Methods

    public void setCalories( int calories ) {
        this.calories = calories;
    }

    public void setExercise( int exercise ) {
        this.exercise = exercise;
    }

    public void setSleep( Sleep sleepObject ) {
        this.SleepObject = sleepObject;
    }

    public void setStands( int stands ) {
        this.stands = stands;
    }
}
