package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.utils.DateConvert;
import com.soopconcept.bugarrr.utils.Validate;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileAct extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Context mContext;
    TextView tv_nama;
    EditText et_birthday, et_tinggi, et_berat_badan, et_kalori, et_olahraga, et_tidur, et_asupan;
    Spinner sp_plan_target, sp_aktivitas;
    RelativeLayout rlprogress;
    Button btn_simpan;
    ImageView back_btn;
    private  String dateview;
    private SimpleDateFormat dateFormatterview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
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
        tv_nama = findViewById(R.id.tv_nama);
        et_birthday = findViewById(R.id.et_birthday);
        et_tinggi = findViewById(R.id.et_tinggi);
        et_berat_badan = findViewById(R.id.et_berat_badan);
        et_kalori = findViewById(R.id.et_kalori);
        et_olahraga = findViewById(R.id.et_olahraga);
        et_tidur = findViewById(R.id.et_tidur);
        et_asupan = findViewById(R.id.et_asupan);
        sp_aktivitas = findViewById(R.id.sp_aktivitas);
        sp_plan_target = findViewById(R.id.sp_plan_target);
        btn_simpan = findViewById(R.id.btn_simpan);
        back_btn = findViewById(R.id.back_btn);
        rlprogress = findViewById(R.id.rlprogress);
    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setupEditText();
        dateFormatterview = new SimpleDateFormat("dd MMMM yyyy");
    }

    private void setupEditText() {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserModel userdata = document.toObject(UserModel.class);
                                tv_nama.setText(userdata.getFirst_name() + " "+ userdata.getLast_name());
                                et_birthday.setText(DateConvert.toDateString(userdata.getDate_of_birth()));
                                dateview = DateConvert.toDateString(userdata.getDate_of_birth());
                                et_tinggi.setText(String.valueOf(userdata.getBody_measurement().getHeight()));
                                et_berat_badan.setText(String.valueOf(userdata.getBody_measurement().getWeight()));
                                et_kalori.setText(String.valueOf(userdata.getActivity_target().getCalories()));
                                et_olahraga.setText(String.valueOf(userdata.getActivity_target().getExercise()));
                                et_asupan.setText(String.valueOf(userdata.getActivity_target().getCalories_intake()));
                                et_tidur.setText(String.valueOf(userdata.getActivity_target().getSleep()));
                                sp_aktivitas.setSelection(userdata.getBody_measurement().getActivity_level() - 1);
                                sp_plan_target.setSelection(userdata.getBody_measurement().getPlan_target() - 1);
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        et_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    try {
                        simpan();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean validate() {
        return (!Validate.cek(et_birthday) && !Validate.cek(et_tinggi) && !Validate.cek(et_berat_badan) && !Validate.cek(et_kalori) && !Validate.cek(et_olahraga) && !Validate.cek(et_asupan) && !Validate.cek(et_tidur));
    }

    private void simpan() throws ParseException {
        Map<String, Object> body_measurement = new HashMap<>();
        body_measurement.put("activity_level", sp_aktivitas.getSelectedItemPosition() + 1);
        body_measurement.put("plan_target", sp_plan_target.getSelectedItemPosition() + 1);
        body_measurement.put("height", Integer.parseInt(et_tinggi.getText().toString()));
        body_measurement.put("weight", Integer.parseInt(et_berat_badan.getText().toString()));

        Map<String, Object> activity_target = new HashMap<>();
        activity_target.put("calories", Integer.parseInt(et_kalori.getText().toString()));
        activity_target.put("calories_intake", Integer.parseInt(et_asupan.getText().toString()));
        activity_target.put("exercise", Integer.parseInt(et_olahraga.getText().toString()));
        activity_target.put("sleep", Integer.parseInt(et_tidur.getText().toString()));

        Map<String, Object> data_value = new HashMap<>();
        data_value.put("date_of_birth", new Timestamp(dateFormatterview.parse(dateview)));
        data_value.put("activity_target", activity_target);
        data_value.put("body_measurement", body_measurement);
        db.collection("users").document(mAuth.getUid())
                .set(data_value, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FancyToast.makeText(mContext,"Berhasil merubah data!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                hide_loading();
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FancyToast.makeText(mContext,"Data gagal tersimpan kedalam database!!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        hide_loading();
                    }
                });
    }

    private void show_loading()
    {
        rlprogress.setVisibility(View.VISIBLE);
    }

    private void hide_loading()
    {
        rlprogress.setVisibility(View.GONE);
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
                        dateview = dateFormatterview.format(date_ship_millis);
                    }
                }
        );
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.black));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }
}