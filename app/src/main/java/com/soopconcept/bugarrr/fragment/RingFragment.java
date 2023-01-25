package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.RingsItem;
import com.soopconcept.bugarrr.item.WorkoutsItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;

public class RingFragment extends Fragment {
    RecyclerView rings;
    Context mContext;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    View root;
    RingsItem adapter;
    Boolean adapter_ready = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_ring, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
    }

    private void initUI() {
        mContext = getContext();
        rings = root.findViewById(R.id.rings);
        rings.setNestedScrollingEnabled(false);
        rings.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getRings();
    }

    private void getRings() {
        final Query query = db.collection("users").document(mAuth.getUid()).collection("activities");
        FirestoreRecyclerOptions<AktivitasModel> options = new FirestoreRecyclerOptions.Builder<AktivitasModel>().setQuery(query, AktivitasModel.class).build();

        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserModel userdata = document.toObject(UserModel.class);
                                FirestoreRecyclerOptions<AktivitasModel> options = new FirestoreRecyclerOptions.Builder<AktivitasModel>().setQuery(query, AktivitasModel.class).build();
                                adapter = new RingsItem(options, userdata.getActivity_target().getCalories(), userdata.getActivity_target().getExercise(), userdata.getActivity_target().getStand());
                                rings.setAdapter(adapter);
                                adapter.startListening();
                                adapter_ready = true;
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter_ready)
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
