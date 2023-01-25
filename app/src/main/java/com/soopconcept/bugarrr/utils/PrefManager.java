package com.soopconcept.bugarrr.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;

    public PrefManager(Context context) {
        this.context = context;
    }

    public void timeSleep(int jam, int menit)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("TimeSleep", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("jam", jam);
        editor.putInt("menit", menit);
        editor.commit();
    }

    public  int getTimeSleepJam(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TimeSleep", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("jam", 0);
    }

    public  int getTimeSleepMenit(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TimeSleep", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("menit", 0);
    }
}
