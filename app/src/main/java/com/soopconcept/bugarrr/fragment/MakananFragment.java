package com.soopconcept.bugarrr.fragment;

import android.content.Context;
import android.icu.number.FormattedNumber;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.dialog.BottomSheetAddFood;
import com.soopconcept.bugarrr.dialog.BotttomSheetMakan;
import com.soopconcept.bugarrr.item.BreakfastItem;
import com.soopconcept.bugarrr.models.FoodModel;
import com.soopconcept.bugarrr.models.LatihanModel;
import com.soopconcept.bugarrr.models.UserModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;
import com.soopconcept.bugarrr.utils.DateConvert;
import com.soopconcept.bugarrr.utils.NumberFormater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MakananFragment extends Fragment implements BottomSheetAddFood.BottomSheetAddMakananListener {
    private View root;
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView tanggal_sekarang, txt_tambah, target_cal, sisa_cal;
    RecyclerView rv_saparan, rv_makan_siang, rv_makan_malam, rv_cemilan, rv_minum;
    Fragment this_fragment;
    BotttomSheetMakan frag;
    int target_calories;
    RelativeLayout rlprogress;
    LinearLayout breakfast_kosong, lunch_kosong, dinner_kosong, snack_kosong, drink_kosong;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_makanan, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        this_fragment = this;
        tanggal_sekarang = root.findViewById(R.id.tanggal_sekarang);
        txt_tambah = root.findViewById(R.id.txt_tambah);
        mContext = getContext();
        target_cal = root.findViewById(R.id.target_cal);
        sisa_cal   = root.findViewById(R.id.sisa_cal);
        rlprogress = root.findViewById(R.id.rlprogress);

        rv_saparan = root.findViewById(R.id.rv_saparan);
        rv_saparan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_saparan.setNestedScrollingEnabled(false);
        rv_saparan.setHasFixedSize(false);

        rv_makan_siang = root.findViewById(R.id.rv_makan_siang);
        rv_makan_siang.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_makan_siang.setNestedScrollingEnabled(false);
        rv_makan_siang.setHasFixedSize(false);

        rv_makan_malam = root.findViewById(R.id.rv_makan_malam);
        rv_makan_malam.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_makan_malam.setNestedScrollingEnabled(false);
        rv_makan_malam.setHasFixedSize(false);

        rv_cemilan = root.findViewById(R.id.rv_cemilan);
        rv_cemilan.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_cemilan.setNestedScrollingEnabled(false);
        rv_cemilan.setHasFixedSize(false);

        rv_minum = root.findViewById(R.id.rv_minum);
        rv_minum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_minum.setNestedScrollingEnabled(false);
        rv_minum.setHasFixedSize(false);

        breakfast_kosong = root.findViewById(R.id.breakfast_kosong);
        lunch_kosong = root.findViewById(R.id.lunch_kosong);
        dinner_kosong = root.findViewById(R.id.dinner_kosong);
        snack_kosong = root.findViewById(R.id.snack_kosong);
        drink_kosong = root.findViewById(R.id.drink_kosong);

    }

    private void initValue() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        tanggal_sekarang.setText(DateConvert.tanggalSekarang());
        setupTopMenu();
        listFood();
    }

    private void setupTopMenu() {
        setupTarget();
    }

    private void setupSisa() {
        db.collection("users")
                .document(mAuth.getUid())
                .collection("workouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<WorkoutsModel> listLatihan = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                listLatihan.add(document.toObject(WorkoutsModel.class));
                            if(listLatihan.size()>0){
                                int total_calories_workouts = 0;
                                for(WorkoutsModel data: listLatihan){
                                    for(int i =0; i < data.getData().size(); i++){
                                        total_calories_workouts = total_calories_workouts + data.getData().get(i).getCalories_burned();
                                    }
                                }
                                getFoodCalories(total_calories_workouts);
                            }
                        } else {
                            Log.d("ggwp", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getFoodCalories(final int total_calories_workouts) {
        db.collection("users")
                .document(mAuth.getUid())
                .collection("food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        sisa_cal.setText(NumberFormater.format(target_calories + total_calories_workouts)+" Kcal");
                        if (task.isSuccessful()) {
                            List<FoodModel> listFood = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                listFood.add(document.toObject(FoodModel.class));
                            if(listFood.size()>0){
                                int total_food_calories = 0, total_breakfast = 0, total_lunch = 0, total_dinner = 0, total_snack = 0, total_drink = 0;
                                for(FoodModel data: listFood){
                                    for(int i =0; i < data.getBreakfast().size(); i++){
                                        total_breakfast = total_breakfast + data.getBreakfast().get(i).getFood_calories();
                                    }
                                    for(int i =0; i < data.getLunch().size(); i++){
                                        total_lunch = total_lunch + data.getLunch().get(i).getFood_calories();
                                    }
                                    for(int i =0; i < data.getDinner().size(); i++){
                                        total_dinner = total_dinner + data.getDinner().get(i).getFood_calories();
                                    }
                                    for(int i =0; i < data.getSnack().size(); i++){
                                        total_snack = total_snack + data.getSnack().get(i).getFood_calories();
                                    }
                                    for(int i =0; i < data.getDrink().size(); i++){
                                        total_drink = total_drink + data.getDrink().get(i).getFood_calories();
                                    }
                                }
                                total_food_calories = total_breakfast + total_lunch + total_dinner + total_snack + total_drink;
                                int total_sisa = target_calories + total_calories_workouts - total_food_calories;
                                sisa_cal.setText(NumberFormater.format(total_sisa)+" Kcal");
                            }
                        } else {
                            Log.d("ggwp", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupTarget() {
        Task<DocumentSnapshot> messageRef = db
                .collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                UserModel userdata = document.toObject(UserModel.class);
                                target_calories = userdata.getActivity_target().getCalories_intake();
                                target_cal.setText(NumberFormater.format(target_calories)+" Kcal");
                                setupSisa();
                            }
                        } else {
                            Toast.makeText(mContext, "No connection!", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void listFood() {
        db.collection("users").document(mAuth.getUid()).collection("food").document(DateConvert.getDateFormatFirebase()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        FoodModel data = document.toObject(FoodModel.class);
                        if(data.getBreakfast().size() != 0){
                            BreakfastItem adapter = new BreakfastItem(mContext, data, 1);
                            rv_saparan.setAdapter(adapter);
                            breakfast_kosong.setVisibility(View.GONE);
                        } else {
                            breakfast_kosong.setVisibility(View.VISIBLE);
                        }
                        if(data.getLunch().size() != 0){
                            BreakfastItem adapter = new BreakfastItem(mContext, data, 2);
                            rv_makan_siang.setAdapter(adapter);
                            lunch_kosong.setVisibility(View.GONE);
                        } else {
                            lunch_kosong.setVisibility(View.VISIBLE);
                        }
                        if(data.getDinner().size() != 0){
                            BreakfastItem adapter = new BreakfastItem(mContext, data, 3);
                            rv_makan_malam.setAdapter(adapter);
                            dinner_kosong.setVisibility(View.GONE);
                        } else {
                            dinner_kosong.setVisibility(View.VISIBLE);
                        }
                        if(data.getSnack().size() != 0){
                            BreakfastItem adapter = new BreakfastItem(mContext, data, 4);
                            rv_cemilan.setAdapter(adapter);
                            snack_kosong.setVisibility(View.GONE);
                        } else {
                            snack_kosong.setVisibility(View.VISIBLE);
                        }
                        if(data.getDrink().size() != 0){
                            BreakfastItem adapter = new BreakfastItem(mContext, data, 5);
                            rv_minum.setAdapter(adapter);
                            drink_kosong.setVisibility(View.GONE);
                        } else {
                            drink_kosong.setVisibility(View.VISIBLE);
                        }
                    } else {
                        breakfast_kosong.setVisibility(View.VISIBLE);
                        lunch_kosong.setVisibility(View.VISIBLE);
                        dinner_kosong.setVisibility(View.VISIBLE);
                        snack_kosong.setVisibility(View.VISIBLE);
                        drink_kosong.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void initEvent() {
        txt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frag = new BotttomSheetMakan(this_fragment);
                frag.show(getChildFragmentManager(), "TambahMakan");
            }
        });
    }

    @Override
    public void onAddFood(Map<String, Object> data) {
        show_loading();
        db.collection("users").document(mAuth.getUid()).collection("food").document(DateConvert.getDateFormatFirebase())
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                frag.dismiss();
                hide_loading();
                listFood();
                FancyToast.makeText(mContext,"Berhasil menambah data!",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hide_loading();
                    }
                });
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
