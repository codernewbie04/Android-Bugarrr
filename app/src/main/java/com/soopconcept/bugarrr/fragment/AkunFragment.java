package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.activity.EditProfileAct;
import com.soopconcept.bugarrr.activity.PengaturanAct;
import com.soopconcept.bugarrr.item.ViewPagerAdapter;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.utils.DateConvert;

public class AkunFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView tv_umur, tv_tinggi, tv_berat, tv_gender, tv_nama;
    Button aktivitas, plan_target, edit_profile;
    ImageView profileimage, pengaturan;
    View root;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_akun, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        profileimage = root.findViewById(R.id.profileimage);
        tv_umur = root.findViewById(R.id.tv_umur);
        tv_tinggi = root.findViewById(R.id.tv_tinggi);
        tv_berat = root.findViewById(R.id.tv_berat);
        tv_gender = root.findViewById(R.id.tv_gender);
        tv_nama = root.findViewById(R.id.tv_nama);
        plan_target = root.findViewById(R.id.plan_target);
        aktivitas = root.findViewById(R.id.aktivitas);
        edit_profile = root.findViewById(R.id.edit_profile);
        pengaturan = root.findViewById(R.id.pengaturan);
        mContext = getContext();
    }

    private void setupViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(position == 0){
                    setupHeight0();
                } else {
                    setupHeight1();
                }
            }
        });
    }

    private void setupHeight0() {
        final LinearLayout layout = root.findViewById(R.id.ll_1);
        db.collection("users").document(mAuth.getUid()).collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            int height = 710 + (count * 430);
                            layout.setMinimumHeight(height);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    private void setupHeight1() {
        final LinearLayout layout = root.findViewById(R.id.ll_1);
        db.collection("users").document(mAuth.getUid()).collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            int height = 710 + (count * 500);
                            layout.setMinimumHeight(height);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getTabs();
        setupViewPager();
        setupHeight0();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataDiri();
    }

    private void getDataDiri() {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserModel userdata = document.toObject(UserModel.class);
                                int umur = DateConvert.getUmur(userdata.getDate_of_birth());
                                tv_umur.setText(umur+" th");
                                tv_tinggi.setText(userdata.getBody_measurement().getHeight()+" cm");
                                tv_berat.setText(userdata.getBody_measurement().getWeight()+ " kg");
                                if(userdata.getBody_measurement().getGender().equals("M")){
                                    tv_gender.setText("Laki-laki");
                                } else {
                                    tv_gender.setText("Perempuan");
                                }
                                tv_nama.setText(userdata.getFirst_name() + " "+userdata.getLast_name());

                                if(userdata.getBody_measurement().getActivity_level() == 1){
                                    aktivitas.setText("Intensitas Rendah");
                                } else if(userdata.getBody_measurement().getActivity_level() == 2){
                                    aktivitas.setText("Intensitas Sedang");
                                } else {
                                    aktivitas.setText("Intensitas Tinggi");
                                }

                                if (userdata.getBody_measurement().getPlan_target() == 1){
                                    plan_target.setText("Menjaga Berat Badan");
                                } else if (userdata.getBody_measurement().getPlan_target() == 2){
                                    plan_target.setText("Menurunkan Berat Badan");
                                } else {
                                    plan_target.setText("Menaikan Berat Badan");
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    public void getTabs(){
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPagerAdapter.addFragment(new RingFragment(),"Riwayat Ring");
                viewPagerAdapter.addFragment(new AsupanFragment(),"Riwayat Asupan");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void initEvent() {
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EditProfileAct.class));
            }
        });

        pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PengaturanAct.class));
            }
        });
    }
}