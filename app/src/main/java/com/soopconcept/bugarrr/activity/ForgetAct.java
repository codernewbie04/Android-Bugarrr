package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.Validate;

public class ForgetAct extends AppCompatActivity {
    ImageView back_btn;
    EditText et_email;
    Button btn_kirim;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mContext = this;
        back_btn = findViewById(R.id.back_btn);
        et_email = findViewById(R.id.et_email);
        btn_kirim = findViewById(R.id.btn_kirim);
    }

    private void initValue() {
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                    kirim_request();
            }
        });
    }

    private boolean validate() {
        return (!Validate.cek(et_email) && !Validate.cekEmail(et_email));
    }

    private void kirim_request() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(et_email.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FancyToast.makeText(mContext,"Request diterima, silakan chek email anda!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            finish();
                        }
                    }
                });
    }
}