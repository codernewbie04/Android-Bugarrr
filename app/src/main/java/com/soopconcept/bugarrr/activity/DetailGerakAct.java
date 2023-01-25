package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.GerakItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.utils.DateConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailGerakAct extends AppCompatActivity {
    private TextView txt_gerak, txt_deskripsi, gerak_kosong;
    private RelativeLayout rlprogress;
    private ImageView rec, back_btn;
    private ProgressBar prog_gerak;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RecyclerView gerak;
    private Context mContext;
    private int counter;
    private Boolean status_to_record = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gerak);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        mContext = this;
        prog_gerak = findViewById(R.id.prog_gerak);
        rec = findViewById(R.id.rec);
        txt_gerak = findViewById(R.id.txt_gerak);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        gerak_kosong = findViewById(R.id.gerak_kosong);
        rlprogress = findViewById(R.id.rlprogress);
        back_btn = findViewById(R.id.back_btn);

        gerak = findViewById(R.id.gerak);
        gerak.setNestedScrollingEnabled(false);
        gerak.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setupGerak();
    }

    private void initEvent() {

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_to_record){
                    rec.setVisibility(View.GONE);
                    counter = 0;
                    prog_gerak.setMax(60);
                    new CountDownTimer(60000, 1000){
                        public void onTick(long millisUntilFinished){
                            counter++;
                            prog_gerak.setProgress(counter);
                        }
                        public  void onFinish(){
                            finishRecord();
                        }
                    }.start();
                } else {
                    FancyToast.makeText(mContext,"Can't be record data!",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void finishRecord() {
        show_loading();
        Map<String, Object> data_value = new HashMap<>();
        data_value.put("stand_data", FieldValue.arrayUnion(Timestamp.now()));
        data_value.put("stands", FieldValue.increment(1));
        db.collection("users").document(mAuth.getUid()).collection("activities").document(DateConvert.getDateFormatFirebase())
                .set(data_value, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hide_loading();
                //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                FancyToast.makeText(mContext,"Pergerakan berhasil direkam!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                setupGerak();
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

    private void setupGerak() {
        DocumentReference query = db.collection("users").document(mAuth.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        UserModel user = document.toObject(UserModel.class);
                        prog_gerak.setMax(user.getActivity_target().getStand());
                        getGerak(user.getActivity_target().getStand());
                    }
                }
            }
        });
    }

    private void getGerak(final int stand) {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid())
                .collection("activities").document(DateConvert.getDateFormatFirebase()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                AktivitasModel data = document.toObject(AktivitasModel.class);
                                int total_gerak = data.getStands();
                                int total_kurang = stand - total_gerak;
                                if(total_kurang < 1){
                                    txt_deskripsi.setText("Hebat! Kamu telah menutup ring olahraga.");
                                }
                                prog_gerak.setProgress(total_gerak);
                                txt_gerak.setText(total_gerak + " Jam");
                                checkAvailForRecord(data.getStand_data());
                                listGerak(data.getStand_data());
                            } else {
                                status_to_record = true;
                            }

                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void checkAvailForRecord(ArrayList<Timestamp> stand_data) {
        if(stand_data.size() > 0){
            Timestamp last_stand = stand_data.get(stand_data.size() - 1);
            Timestamp now = Timestamp.now();
            int selisih = (int) (now.getSeconds() - last_stand.getSeconds());
            if((selisih / 60)/60 <= 0){
                rec.setVisibility(View.GONE);
            } else {
                rec.setVisibility(View.VISIBLE);
                status_to_record = true;
            }
        }
    }

    private void listGerak(ArrayList<Timestamp> stand_data) {
        if(stand_data.size() > 0){
            gerak.setVisibility(View.VISIBLE);
            gerak_kosong.setVisibility(View.GONE);
            GerakItem adapter = new GerakItem(mContext, stand_data);
            gerak.setAdapter(adapter);
        } else {
            status_to_record = true;
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