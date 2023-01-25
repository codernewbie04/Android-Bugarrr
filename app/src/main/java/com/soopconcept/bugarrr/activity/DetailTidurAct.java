package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.dialog.BottomSheetTidur;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.Sleep;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.utils.DateConvert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailTidurAct extends AppCompatActivity implements BottomSheetTidur.BottomSheetListener {
    private TextView txt_tambah, txt_tidur, tidur_kosong, txt_tanggal, txt_waktu_tidur, txt_total_tidur;
    private ProgressBar prog_tidur;
    private RelativeLayout rl_tidur;
    private FirebaseAuth mAuth;
    private RelativeLayout rlprogress, rv_tidur;
    private FirebaseFirestore db;
    private Context mContext;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tidur);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        txt_tambah = findViewById(R.id.txt_tambah);
        prog_tidur = findViewById(R.id.prog_tidur);
        txt_tidur = findViewById(R.id.txt_tidur);
        rv_tidur = findViewById(R.id.rv_tidur);
        tidur_kosong = findViewById(R.id.tidur_kosong);
        rlprogress = findViewById(R.id.rlprogress);
        back_btn = findViewById(R.id.back_btn);
        rl_tidur = findViewById(R.id.rl_tidur);
        txt_tanggal = findViewById(R.id.txt_tanggal);
        txt_waktu_tidur = findViewById(R.id.txt_waktu_tidur);
        txt_total_tidur = findViewById(R.id.txt_total_tidur);
        mContext = this;
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        setupTidur();
    }

    private void initEvent() {
        txt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetTidur frag = new BottomSheetTidur();
                frag.show(getSupportFragmentManager(), "TambahTidur");
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupTidur() {
        DocumentReference query = db.collection("users").document(mAuth.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        UserModel user = document.toObject(UserModel.class);
                        prog_tidur.setMax(user.getActivity_target().getSleep());
                        getTidur(user.getActivity_target().getSleep());
                    }
                }
            }
        });
    }

    private void getTidur(final int sleep) {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid())
                .collection("activities").document(DateConvert.getDateFormatFirebase()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                AktivitasModel data = document.toObject(AktivitasModel.class);
                                int total_tidur = 0;
                                if(data.getSleep() != null){
                                    rv_tidur.setVisibility(View.VISIBLE);
                                    tidur_kosong.setVisibility(View.GONE);
                                    total_tidur = DateConvert.getSelisihMenit(data.getSleep().getStart_time(), data.getSleep().getEnd_time());
                                    listTidur(data.getSleep());
                                }
                                int total_kurang = ((sleep * 60) - total_tidur) / 60;
                                prog_tidur.setProgress(total_tidur/60);
                                if(total_tidur % 60 == 0){
                                    txt_tidur.setText((total_tidur / 60) + " Jam");
                                } else {
                                    txt_tidur.setText((total_tidur / 60) + " Jam "+ (total_tidur % 60)+ " Menit");
                                }
                            }

                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void listTidur(Sleep sleep) {
        txt_tanggal.setText(DateConvert.getTanggalBulan(sleep.getStart_time()) + " - " + DateConvert.getTanggalBulan(sleep.getEnd_time()));
        txt_waktu_tidur.setText(DateConvert.getJam(sleep.getStart_time().getSeconds()) + " - " + DateConvert.getJam(sleep.getEnd_time().getSeconds()));
        int menit_tidur = (int) (sleep.getEnd_time().getSeconds() - sleep.getStart_time().getSeconds()) / 60;
        if(menit_tidur % 60 == 0){
            txt_total_tidur.setText(menit_tidur/60 + " Jam");
        } else {
            txt_total_tidur.setText(menit_tidur/60 + " Jam " + menit_tidur % 60 + " Menit");
        }
    }

    @Override
    public void onTambahClicked(Map<String, Timestamp> data) {
        Map<String, Object> data_value = new HashMap<>();
        if(data.get("start") != null && data.get("end") != null){
            show_loading();
            data_value.put("end_time", data.get("end"));
            data_value.put("start_time", data.get("start"));
            Map<String, Object> insert = new HashMap<>();
            insert.put("sleep", data_value);
            db.collection("users").document(mAuth.getUid()).collection("activities").document(DateConvert.getDateFormatFirebase())
                    .set(insert, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FancyToast.makeText(mContext,"Berhasil menambah data!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                    hide_loading();
                    setupTidur();
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
    }

    private void show_loading()
    {
        rlprogress.setVisibility(View.VISIBLE);
    }

    private void hide_loading()
    {
        rlprogress.setVisibility(View.GONE);
    }
}