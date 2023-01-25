package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.Validate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PersonalInfoAct extends AppCompatActivity {
    private RadioGroup gender_selector, level_act_selector;
    private RadioButton rendah, sedang, tinggi;
    private EditText et_tinggi, et_berat_badan;
    private RelativeLayout rlprogress;
    private FirebaseAuth mAuth;
    private Button btn_lanjut;
    private int act_level = 1;
    private float poin = (float) 1.37;
    private Context mContext;
    private boolean disableback;
    TextView kategori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mContext            = this;
        rendah              = findViewById(R.id.rendah);
        sedang              = findViewById(R.id.sedang);
        tinggi              = findViewById(R.id.tinggi);
        kategori            = findViewById(R.id.kategori);
        rlprogress          = findViewById(R.id.rlprogress);
        btn_lanjut          = findViewById(R.id.btn_lanjut);
        et_tinggi           = findViewById(R.id.et_tinggi);
        et_berat_badan      = findViewById(R.id.et_berat_badan);
        gender_selector     = findViewById(R.id.gender_selector);
        level_act_selector  = findViewById(R.id.level_act_selector);
    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        disableback = false;
        if(mAuth.getCurrentUser() == null){
            mAuth.signOut();
            FancyToast.makeText(mContext,"Harap login ulang!",FancyToast.LENGTH_LONG,FancyToast.WARNING,false).show();
            initValue();
        }
    }

    private void initEvent() {
        btn_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate_form())
                    lanjut();
            }
        });
        rendah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori.setText("Saya biasanya berolahraga 1x dengan intensitas rendah atau tidak berolahraga dalam seminggu. Dan belum pernah mengikuti program latihan rutin.");
                act_level = 3;
                poin = (float) 1.37;
            }
        });
        sedang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori.setText("Saya biasanya berolahraga 2-3x dalam seminggu dengan intensitas sedang. Dan belum pernah atau pernah mengikuti program latihan rutin.");
                act_level = 2;
                poin = (float) 1.55;
            }
        });
        tinggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kategori.setText("Saya biasanya berolahraga 3-5x atau setiap hari dalam seminggu dengan intensitas tinggi. Dan pernah mengikuti program latihan rutin.");
                act_level = 1;
                poin = (float) 1.725;
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
        return (!Validate.cek(et_tinggi) && !Validate.cek(et_berat_badan));
    }

    private void lanjut() {
        int genderSelected = gender_selector.getCheckedRadioButtonId();
        int levelSelected = level_act_selector.getCheckedRadioButtonId();
        RadioButton genderRadioBtn = (RadioButton)findViewById(genderSelected);
        RadioButton levelRadioBtn = (RadioButton)findViewById(levelSelected);

        String gender = "M";
        int umur = 19;
        float cal_intake = (float) 0.0;
        if(genderRadioBtn.equals("Perempuan")){
            gender = "F";
            cal_intake = (float) (655.1 + (4.35 * Integer.parseInt(et_berat_badan.getText().toString())) + (4.7 * Integer.parseInt(et_tinggi.getText().toString())) - (4.7 * umur) * poin);
        } else {
            gender = "M";
            cal_intake = (float) (66 + (6.2 * Integer.parseInt(et_berat_badan.getText().toString())) + (12.7 * Integer.parseInt(et_tinggi.getText().toString())) - (6.76 * umur) * poin);
        }
        show_loading();
        addToFirestore(gender, act_level, cal_intake);
    }
    private int round(float d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Integer.parseInt(twoDForm.format(d));
    }
    private void addToFirestore(String gender, int act_level, float cal_intake) {
        Map<String, Object> body_measurement = new HashMap<>();
        Map<String, Object> target_value = new HashMap<>();
        Map<String, Object> body_value = new HashMap<>();

        // Menambah untuk filed body_measurment
        body_value.put("activity_level", act_level);
        body_value.put("gender", gender);
        body_value.put("height", Integer.parseInt(et_tinggi.getText().toString()));
        body_value.put("weight", Integer.parseInt(et_berat_badan.getText().toString()));

        //Menambah untuk field activity_target
        target_value.put("calories", 300);
        target_value.put("calories_intake", round(cal_intake));
        target_value.put("exercise", 30);
        target_value.put("sleep", 8);
        target_value.put("stand", 12);


        body_measurement.put("body_measurement", body_value);
        body_measurement.put("activity_target", target_value);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getUid())
                .update(body_measurement)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(mContext, TargetAct.class));
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