package com.soopconcept.bugarrr.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class Validate {
    public static boolean cek(EditText et) {
        View focusView = null;
        Boolean cancel=false;
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            et.setError("Inputan Harus Di Isi");
            focusView = et;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }

    public static boolean cekEmail(EditText email){
        View focusView = null;
        Boolean cancel=false;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Email tidak valid");
            focusView = email;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }
}
