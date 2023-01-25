package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.Validate;

public class LoginAct extends AppCompatActivity {
    private EditText et_email, et_password;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button btn_masuk;
    private Context mContext;
    ImageView back_btn;
    TextView tv_forget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        mContext    = this;
        et_email    = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_masuk   = findViewById(R.id.btn_masuk);
        back_btn = findViewById(R.id.back_btn);
        tv_forget = findViewById(R.id.tv_forget);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initValue() {
        db      = FirebaseFirestore.getInstance();
        mAuth   = FirebaseAuth.getInstance();
    }

    private void initEvent() {
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLogin()) 
                    login();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ForgetAct.class));
            }
        });
    }

    private void login() {
        mAuth.signInWithEmailAndPassword(et_email.getText().toString(), et_password.getText().toString()).addOnCompleteListener(
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
                            } catch (Exception e) {
                                Log.d("Login", "onComplete: " + e.getMessage());
                                FancyToast.makeText(mContext,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                            }
                        } else {
                            DocumentReference doc = db.collection("users").document(task.getResult().getUser().getUid());
                            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            if(!document.contains("body_measurement")){
                                                startActivity(new Intent(mContext, PersonalInfoAct.class));
                                                finishAffinity();
                                            }  else if(!document.contains("body_measurement.plan_target")){
                                                startActivity(new Intent(mContext, TargetAct.class));
                                                finishAffinity();
                                            } else {
                                                startActivity(new Intent(mContext, HomeAct.class));
                                                finishAffinity();
                                            }
                                        } else {
                                            FancyToast.makeText(mContext,"Akun bermasalah!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                        }
                                    } else {
                                        FancyToast.makeText(mContext,"No Connection!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                                    }
                                }
                            });
                        }
                    }
                }
        );
    }

    private boolean validateLogin() {
        return (!Validate.cek(et_email)&&!Validate.cek(et_password)&&!Validate.cekEmail(et_email));
    }
}