package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.AsupanItem;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.FoodModel;

public class AsupanFragment extends Fragment{
    View root;
    Context mContext;
    RecyclerView asupan;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    AsupanItem adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_asupan, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
    }

    private void initUI() {
        mContext = getContext();
        asupan = root.findViewById(R.id.asupan);
        asupan.setNestedScrollingEnabled(false);
        asupan.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getAsupan();
    }

    private void getAsupan() {
        Query query = db.collection("users").document(mAuth.getUid()).collection("food");
        FirestoreRecyclerOptions<FoodModel> options = new FirestoreRecyclerOptions.Builder<FoodModel>().setQuery(query, FoodModel.class).build();
        adapter = new AsupanItem(options);
        asupan.setAdapter(adapter);
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
