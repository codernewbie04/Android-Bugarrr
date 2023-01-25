package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.activity.RiwayatActivitasAct;
import com.soopconcept.bugarrr.item.AktivitasItem;
import com.soopconcept.bugarrr.item.TipsItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.TipsModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;
import com.soopconcept.bugarrr.utils.DateConvert;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BerandaFragment extends Fragment {
    private View root;
    private Context mContext;
    private RecyclerView tips, workouts;
    private TipsItem adapter;
    private AktivitasItem adapterAktivitas;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String dateFirebase;
    private TextView txt_calories, txt_olahraga, txt_gerak, txt_target_kalori,
            txt_target_olahraga, txt_target_gerak, total_tidur, waktu_tidur, tanggal_sekarang,
            aktivitas_kosong, riwayat_activitas;
    private LinearLayout ll_tidur;
    private ProgressBar prog_kalori, prog_olahraga, prog_gerak;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        txt_target_kalori = root.findViewById(R.id.txt_target_kalori);
        txt_target_olahraga = root.findViewById(R.id.txt_target_olahraga);
        txt_target_gerak = root.findViewById(R.id.txt_target_gerak);

        txt_calories = root.findViewById(R.id.txt_calories);
        txt_olahraga = root.findViewById(R.id.txt_olahraga);
        txt_gerak = root.findViewById(R.id.txt_gerak);

        prog_kalori = root.findViewById(R.id.prog_kalori);
        prog_olahraga = root.findViewById(R.id.prog_olahraga);
        prog_gerak = root.findViewById(R.id.prog_gerak);

        tanggal_sekarang = root.findViewById(R.id.tanggal_sekarang);
        waktu_tidur = root.findViewById(R.id.waktu_tidur);
        total_tidur = root.findViewById(R.id.total_tidur);

        aktivitas_kosong = root.findViewById(R.id.aktivitas_kosong);
        riwayat_activitas = root.findViewById(R.id.riwayat_activitas);
        ll_tidur = root.findViewById(R.id.ll_tidur);

        workouts = root.findViewById(R.id.workouts);
        workouts.setNestedScrollingEnabled(false);
        workouts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        tips = root.findViewById(R.id.tips);
        tips.setNestedScrollingEnabled(false);
        tips.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initValue() {
        mContext = getContext();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM");
        SimpleDateFormat dateFormatFirebase = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        dateFirebase = DateConvert.getDateFormatFirebase();

        tanggal_sekarang.setText(date);
        getTips();
        setUpActivitas();
        listAktivitas();
    }

    private void listAktivitas() {
        DocumentReference query = db.collection("users").document(mAuth.getUid()).collection("workouts").document(dateFirebase);
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
                }
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
                                txt_target_kalori.setText("/"+String.valueOf(userdata.getActivity_target().getCalories())+" Kcal");
                                prog_kalori.setMax(userdata.getActivity_target().getCalories());

                                //Setting target olahraga
                                txt_target_olahraga.setText("/"+String.valueOf(userdata.getActivity_target().getExercise())+" Menit");
                                prog_olahraga.setMax(userdata.getActivity_target().getExercise());

                                //Setting target gerak
                                txt_target_gerak.setText("/"+String.valueOf(userdata.getActivity_target().getStand())+" Jam");
                                prog_gerak.setMax(userdata.getActivity_target().getStand());
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
                                if(document.contains("sleep")) {
                                    ll_tidur.setVisibility(View.VISIBLE);
                                    int menit_tidur = (int) (data.getSleep().getEnd_time().getSeconds() - data.getSleep().getStart_time().getSeconds()) / 60;
                                    total_tidur.setVisibility(View.VISIBLE);
                                    if (menit_tidur % 60 == 0) {
                                        total_tidur.setText(menit_tidur / 60 + " Jam");
                                    } else {
                                        total_tidur.setText(menit_tidur / 60 + " Jam " + menit_tidur % 60 + " Menit");
                                    }
                                    waktu_tidur.setText(DateConvert.getJam(data.getSleep().getStart_time().getSeconds()) + " - " + DateConvert.getJam(data.getSleep().getEnd_time().getSeconds()));
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void getTips() {
        Query query = db.collection("tips").orderBy("created_at", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TipsModel> options = new FirestoreRecyclerOptions.Builder<TipsModel>().setQuery(query, TipsModel.class).build();
        adapter = new TipsItem(options);
        tips.setAdapter(adapter);
    }

    private void initEvent() {
        riwayat_activitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RiwayatActivitasAct.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

}