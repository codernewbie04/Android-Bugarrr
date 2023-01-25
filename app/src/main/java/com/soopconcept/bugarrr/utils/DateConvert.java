package com.soopconcept.bugarrr.utils;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConvert {
    public static String getDateFormatFirebase()
    {
        SimpleDateFormat dateFormatFirebase = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        return dateFormatFirebase.format(calendar.getTime());
    }

    public static int getUmur(Timestamp time)
    {
        long selisih = Timestamp.now().getSeconds() - time.getSeconds();
        return (int) (selisih / 60 / 60 / 24 / 365);
    }

    public static String getTanggalBulan(Timestamp time)
    {
        SimpleDateFormat sfd = new SimpleDateFormat("d MMM");
        return sfd.format(new Date(time.getSeconds() *1000));
    }

    public static String getJam(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("HH:mm", cal).toString();
        return date;
    }

    public static int getSelisihMenit(Timestamp start, Timestamp end){
        long total = (int) (end.getSeconds() - start.getSeconds());
        return (int) (total / 60);
    }

    public static int getSelisihJam(Timestamp start, Timestamp end)
    {
        long total = (int) (end.getSeconds() - start.getSeconds());
        return (int) (total / 60 / 60);
    }

    public static String  tanggalSekarang()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    public static String toDateString(Timestamp date_of_birth) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd MMMM yyyy");
        return sfd.format(new Date(date_of_birth.getSeconds() * 1000));
    }
}
