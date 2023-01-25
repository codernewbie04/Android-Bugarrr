package com.soopconcept.bugarrr.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soopconcept.bugarrr.R;

public class FinishRegisterAct extends AppCompatActivity {
    private Button btn_masuk;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        btn_masuk = findViewById(R.id.btn_masuk);
    }

    private void initValue() {
        mContext = this;
    }

    private void initEvent() {
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HomeAct.class));
                finishAffinity();
            }
        });
    }
}