package com.soopconcept.bugarrr.utils;

import java.text.DecimalFormat;

public class NumberFormater {
    public static final String format(int number){
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(number);
    }
}
