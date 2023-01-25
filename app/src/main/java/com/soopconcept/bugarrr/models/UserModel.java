package com.soopconcept.bugarrr.models;

import com.google.firebase.Timestamp;

public class UserModel {
    Activity_target activity_target;
    Body_measurement body_measurement;
    private Timestamp date_of_birth;
    private String email;
    private String first_name;
    private String last_name;

    public UserModel(){}

    public UserModel(Activity_target activity_target, Body_measurement body_measurement, Timestamp date_of_birth, String email, String first_name, String last_name)
    {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.body_measurement = body_measurement;
        this.activity_target = activity_target;
    }

    public Activity_target getActivity_target() {
        return activity_target;
    }

    public Body_measurement getBody_measurement() {
        return body_measurement;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Timestamp getDate_of_birth() {
        return date_of_birth;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate_of_birth(Timestamp date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setActivity_target(Activity_target activity_target) {
        this.activity_target = activity_target;
    }
}
