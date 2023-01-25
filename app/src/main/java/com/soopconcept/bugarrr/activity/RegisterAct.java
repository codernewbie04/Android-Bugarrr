package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.Validate;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterAct extends AppCompatActivity {
    private EditText et_fist_name, et_last_name, et_birthday, et_email, et_password;
    private FirebaseAuth mAuth;
    private Button btn_lanjut;
    private Context mContext;
    private RelativeLayout rlprogress;
    private  String dateview;
    private SimpleDateFormat dateFormatter, dateFormatterview;
    private boolean disableback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        mContext        = this;
        et_fist_name    = findViewById(R.id.et_fist_name);
        et_last_name    = findViewById(R.id.et_last_name);
        et_birthday     = findViewById(R.id.et_birthday);
        et_email        = findViewById(R.id.et_email);
        et_password     = findViewById(R.id.et_password);
        btn_lanjut      = findViewById(R.id.btn_lanjut);
        rlprogress      = findViewById(R.id.rlprogress);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initValue() {
        disableback = false;
        mAuth = FirebaseAuth.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatterview = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    }

    private void initEvent() {
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate_form())
                    lanjut();
            }
        });
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_birthday.getWindowToken(), 0);
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_birthday, InputMethodManager.SHOW_IMPLICIT);
                datePicker();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (disableback) {
            return;
        } else {
            finish();
        }
    }

    private void datePicker() {
       DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        et_birthday.setText(dateFormatterview.format(date_ship_millis));
                        dateview = dateFormatter.format(date_ship_millis);
                    }
                }
        );
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.black));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void show_loading()
    {
        disableback = true;
        rlprogress.setVisibility(View.VISIBLE);
    }

    private void hide_loading()
    {
        disableback = false;
        rlprogress.setVisibility(View.GONE);
    }

    private boolean validate_form() {
        return (!Validate.cek(et_fist_name) && !Validate.cek(et_last_name) && !Validate.cek(et_birthday) && !Validate.cek(et_email)  && !Validate.cekEmail(et_email) && !Validate.cek(et_password));
    }

    private void lanjut() {
        show_loading();
        mAuth.createUserWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString())
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException weakPassword){
                                        FancyToast.makeText(mContext,"Password terlalu lemah!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                    } catch (FirebaseAuthUserCollisionException existEmail){
                                        FancyToast.makeText(mContext,"Email sudah digunakan!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                    } catch (Exception e) {
                                        FancyToast.makeText(mContext,"Error " +  e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                        Log.d("RegisterAct", "onComplete: " + e.getMessage());
                                    }
                                    hide_loading();
                                } else {
                                    try {
                                        addToFirestore(task.getResult().getUser());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                );
    }

    private void addToFirestore(FirebaseUser user) throws ParseException {
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("email", user.getEmail());
        userdata.put("first_name", et_fist_name.getText().toString());
        userdata.put("last_name", et_last_name.getText().toString());
        userdata.put("date_of_birth", new Timestamp(dateFormatter.parse(dateview)));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .set(userdata)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(mContext, PersonalInfoAct.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FancyToast.makeText(mContext,"Data gagal tersimpan kedalam database!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        hide_loading();
                    }
                });
    }
}