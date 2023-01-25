package com.soopconcept.bugarrr.models;

public class Body_measurement {
    private int activity_level;
    private String gender;
    private int height;
    private int plan_target;
    private int weight;

    public Body_measurement(){}

    public Body_measurement(int act, String gender, int height, int plan, int weight)
    {
        this.activity_level = act;
        this.gender = gender;
        this.height = height;
        this.plan_target = plan;
        this.weight = weight;
    }

    public int getActivity_level() {
        return activity_level;
    }

    public int getHeight() {
        return height;
    }

    public int getPlan_target() {
        return plan_target;
    }

    public int getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }
}
