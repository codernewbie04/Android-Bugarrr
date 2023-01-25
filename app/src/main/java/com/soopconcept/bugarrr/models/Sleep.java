package com.soopconcept.bugarrr.models;


import com.google.firebase.Timestamp;

public class Sleep {
    private Timestamp end_time;
    private Timestamp start_time;

    public Sleep(){}

    public Sleep(Timestamp end, Timestamp start_time)
    {
        this.end_time = end;
        this.start_time = start_time;
    }
    // Getter Methods

    public Timestamp getEnd_time() {
        return end_time;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    // Setter Methods


}
