package com.soopconcept.bugarrr.models;

import java.util.ArrayList;

public class WorkoutsModel {
    ArrayList<LatihanModel> data = new ArrayList<LatihanModel>();

    public WorkoutsModel(){}

    public WorkoutsModel(ArrayList<LatihanModel> data){
        this.data = data;
    }
    public ArrayList<LatihanModel> getData() {
        return data;
    }
}
