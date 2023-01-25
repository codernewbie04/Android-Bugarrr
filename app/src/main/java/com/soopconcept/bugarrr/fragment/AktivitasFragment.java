package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.activity.DetailGerakAct;
import com.soopconcept.bugarrr.activity.DetailKaloriAct;
import com.soopconcept.bugarrr.activity.DetailOlahragaAct;
import com.soopconcept.bugarrr.activity.DetailTidurAct;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.utils.DateConvert;

public class AktivitasFragment extends Fragment {
    private View root;
    private String dateFirebase;
    private FirebaseAuth mAuth;
    private Context mContext;
    private FirebaseFirestore db;
    private RelativeLayout rl_kalori, rl_olahraga, rl_gerak, rl_tidur;
    private ProgressBar prog_kalori, prog_olahraga, prog_gerak, prog_tidur;
    private TextView txt_calories, txt_olahraga, txt_gerak, txt_target_kalori,
            txt_target_olahraga, txt_target_gerak, tanggal_sekarang,
            txt_target_tidur, txt_tidur;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_aktivitas, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        rl_kalori = root.findViewById(R.id.rl_kalori);
        rl_olahraga = root.findViewById(R.id.rl_olahraga);
        rl_gerak = root.findViewById(R.id.rl_gerak);
        rl_tidur = root.findViewById(R.id.rl_tidur);

        txt_target_kalori = root.findViewById(R.id.txt_target_kalori);
        txt_target_olahraga = root.findViewById(R.id.txt_target_olahraga);
        txt_target_gerak = root.findViewById(R.id.txt_target_gerak);
        txt_target_tidur = root.findViewById(R.id.txt_target_tidur);

        txt_calories = root.findViewById(R.id.txt_calories);
        txt_olahraga = root.findViewById(R.id.txt_olahraga);
        txt_gerak = root.findViewById(R.id.txt_gerak);
        txt_tidur = root.findViewById(R.id.txt_tidur);


        prog_kalori = root.findViewById(R.id.prog_kalori);
        prog_olahraga = root.findViewById(R.id.prog_olahraga);
        prog_gerak = root.findViewById(R.id.prog_gerak);
        prog_tidur = root.findViewById(R.id.prog_tidur);

        tanggal_sekarang = root.findViewById(R.id.tanggal_sekarang);
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mContext = getContext();
        dateFirebase = DateConvert.getDateFormatFirebase();
        tanggal_sekarang.setText(DateConvert.tanggalSekarang());

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActivitas();
    }

    private void initEvent() {
        rl_kalori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DetailKaloriAct.class));
            }
        });

        rl_olahraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DetailOlahragaAct.class));
            }
        });

        rl_gerak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DetailGerakAct.class));
            }
        });

        rl_tidur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, DetailTidurAct.class));
            }
        });
    }

    private void setUpActivitas() {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserModel userdata = document.toObject(UserModel.class);
                                //Setting target kalori
                                txt_target_kalori.setText(" / "+String.valueOf(userdata.getActivity_target().getCalories())+" Kcal");
                                prog_kalori.setMax(userdata.getActivity_target().getCalories());

                                //Setting target olahraga
                                txt_target_olahraga.setText(" / "+String.valueOf(userdata.getActivity_target().getExercise())+" Menit");
                                prog_olahraga.setMax(userdata.getActivity_target().getExercise());

                                //Setting target gerak
                                txt_target_gerak.setText(" / "+String.valueOf(userdata.getActivity_target().getStand())+" Jam");
                                prog_gerak.setMax(userdata.getActivity_target().getStand());

                                //Setting target tidur
                                txt_target_tidur.setText(" / "+String.valueOf(userdata.getActivity_target().getSleep())+" Jam");
                                prog_tidur.setMax(userdata.getActivity_target().getSleep());
                            }
                            getActivitas();
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void getActivitas() {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid())
                .collection("activities").document(dateFirebase).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                AktivitasModel data = document.toObject(AktivitasModel.class);
                                //Setting Progres kalori
                                txt_calories.setText(String.valueOf(data.getCalories()));
                                prog_kalori.setProgress(data.getCalories());

                                //Setting Progres olahraga
                                txt_olahraga.setText(String.valueOf(data.getExercise()));
                                prog_olahraga.setProgress(data.getExercise());

                                //Setting Progres Gerak
                                txt_gerak.setText(String.valueOf(data.getStands()));
                                prog_gerak.setProgress(data.getStands());
                                if(document.contains("sleep")){
                                    int menit_tidur = (int) (data.getSleep().getEnd_time().getSeconds() - data.getSleep().getStart_time().getSeconds()) / 60;
                                    int waktu_tidur = menit_tidur/60;
                                    if(menit_tidur % 60 == 0){
                                        txt_tidur.setText(menit_tidur/60 + " Jam");

                                    } else {
                                        txt_tidur.setText(menit_tidur/60 + " Jam " + menit_tidur % 60 + " Menit");
                                    }
                                    prog_tidur.setProgress(waktu_tidur);
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}