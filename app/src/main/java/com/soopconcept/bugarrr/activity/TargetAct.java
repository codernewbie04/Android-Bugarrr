package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;

import java.util.HashMap;
import java.util.Map;

public class TargetAct extends AppCompatActivity {
    private RadioButton target1, target2, target3;
    private RelativeLayout rlprogress;
    private FirebaseAuth mAuth;
    private Button btn_daftar;
    private Context mContext;
    private int _target = 1;
    private boolean disableback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        target1     = findViewById(R.id.target1);
        target2     = findViewById(R.id.target2);
        target3     = findViewById(R.id.target3);
        btn_daftar  = findViewById(R.id.btn_daftar);
        rlprogress  = findViewById(R.id.rlprogress);
    }

    private void initValue() {
        mContext = this;
        mAuth = FirebaseAuth.getInstance();
        disableback = false;
    }

    private void initEvent() {
        target1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _target = 1;
            }
        });

        target2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _target = 2;
            }
        });

        target3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _target = 3;
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar();
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

    private void daftar() {
        show_loading();
        Map<String, Object> data = new HashMap<>();
        data.put("body_measurement.plan_target", _target);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(mAuth.getUid())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(mContext, FinishRegisterAct.class));
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

    void logout(){
        Intent goto_getstarted = new Intent(mContext, GetStartedAct.class);
        startActivity(goto_getstarted);
        finishAffinity();
    }
}