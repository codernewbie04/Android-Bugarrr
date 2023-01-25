package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.item.BeritaItem;
import com.soopconcept.bugarrr.item.TipsItem;
import com.soopconcept.bugarrr.models.TipsModel;
import com.soopconcept.bugarrr.utils.DateConvert;


public class BeritaFragment extends Fragment {
    private TextView tanggal_sekarang;
    private RecyclerView tips, berita;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Context mContext;
    private TipsItem adapterTips;
    private BeritaItem adapterBerita;
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_berita, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        tanggal_sekarang = root.findViewById(R.id.tanggal_sekarang);

        tips = root.findViewById(R.id.tips);
        tips = root.findViewById(R.id.tips);
        tips.setNestedScrollingEnabled(false);
        tips.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        berita = root.findViewById(R.id.berita);
        berita.setNestedScrollingEnabled(false);
        berita.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void initValue() {
        mContext = getContext();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tanggal_sekarang.setText(DateConvert.tanggalSekarang());

        getTips();
        getBerita();
    }

    private void initEvent() {
    }

    private void getTips() {
        Query query = db.collection("tips").orderBy("created_at", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TipsModel> options = new FirestoreRecyclerOptions.Builder<TipsModel>().setQuery(query, TipsModel.class).build();
        adapterTips = new TipsItem(options);
        tips.setAdapter(adapterTips);
    }

    private void getBerita() {
        Query query = db.collection("news").orderBy("created_at", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<TipsModel> options = new FirestoreRecyclerOptions.Builder<TipsModel>().setQuery(query, TipsModel.class).build();
        adapterBerita = new BeritaItem(options);
        berita.setAdapter(adapterBerita);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterTips.startListening();
        adapterBerita.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapterTips != null) {
            adapterTips.stopListening();
        }
        if (adapterBerita != null) {
            adapterBerita.stopListening();
        }
    }
}