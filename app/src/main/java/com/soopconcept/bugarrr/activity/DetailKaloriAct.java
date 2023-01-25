package com.soopconcept.bugarrr.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.AktivitasItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;
import com.soopconcept.bugarrr.utils.DateConvert;

public class DetailKaloriAct extends AppCompatActivity {
    ImageView back_btn;
    ProgressBar prog_kalori;
    TextView txt_kalori, txt_deskripsi, aktivitas_kosong;
    RecyclerView workouts;
    Context mContext;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kalori);
        initStart();
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        mContext = this;
        back_btn = findViewById(R.id.back_btn);
        prog_kalori = findViewById(R.id.prog_kalori);
        txt_kalori = findViewById(R.id.txt_kalori);
        txt_deskripsi = findViewById(R.id.txt_deskripsi);
        aktivitas_kosong = findViewById(R.id.aktivitas_kosong);

        workouts = findViewById(R.id.workouts);
        workouts.setNestedScrollingEnabled(false);
        workouts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        listAktivitas();
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void listAktivitas() {
        DocumentReference query = db.collection("users").document(mAuth.getUid()).collection("workouts").document(DateConvert.getDateFormatFirebase());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        WorkoutsModel latihan = document.toObject(WorkoutsModel.class);
                        if(latihan.getData().size() > 0){
                            workouts.setVisibility(View.VISIBLE);
                            aktivitas_kosong.setVisibility(View.GONE);
                            AktivitasItem adapter = new AktivitasItem(mContext, latihan);
                            workouts.setAdapter(adapter);
                        }
                    }
                    setupUI();
                }
            }
        });
    }

    private void setupUI() {
        DocumentReference query = db.collection("users").document(mAuth.getUid());
        query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        UserModel user = document.toObject(UserModel.class);
                        prog_kalori.setMax(user.getActivity_target().getCalories());
                        getUI(user.getActivity_target().getCalories());
                    }
                }
            }
        });
    }

    private void getUI(final int target_calories) {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid())
                .collection("activities").document(DateConvert.getDateFormatFirebase()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                AktivitasModel data = document.toObject(AktivitasModel.class);
                                int total_calories_burned = data.getCalories();
                                int total_kurang = target_calories - total_calories_burned;
                                if(total_kurang > 0){
                                    txt_deskripsi.setText("Latihan sebanyak "+ total_kurang +" Kcal untuk menutup ring kamu. Kalori akan otomatis terhitung pada saat kamu melakukan gerakan berjalan atau yang lebih intens.");
                                } else {
                                    txt_deskripsi.setText("Hebat! Kamu telah menutup ring kalori.");
                                }
                                prog_kalori.setProgress(total_calories_burned);
                                txt_kalori.setText(total_calories_burned + " Kcal");
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}