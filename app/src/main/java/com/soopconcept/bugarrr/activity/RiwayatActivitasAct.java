package com.soopconcept.bugarrr.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.TipsItem;
import com.soopconcept.bugarrr.item.WorkoutsItem;
import com.soopconcept.bugarrr.models.TipsModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;

public class RiwayatActivitasAct extends AppCompatActivity {
    private RecyclerView workouts;
    private WorkoutsItem adapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ImageView back_btn;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_activitas);
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

        workouts = findViewById(R.id.workouts);
        workouts.setNestedScrollingEnabled(false);
        workouts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getWorkouts();
    }

    private void getWorkouts() {
        Query query = db.collection("users").document(mAuth.getUid()).collection("workouts");
        FirestoreRecyclerOptions<WorkoutsModel> options = new FirestoreRecyclerOptions.Builder<WorkoutsModel>().setQuery(query, WorkoutsModel.class).build();
        adapter = new WorkoutsItem(options);
        workouts.setAdapter(adapter);
    }

    private void initEvent() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}