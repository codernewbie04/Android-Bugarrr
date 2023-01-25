package com.soopconcept.bugarrr.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.Validate;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetAddFood extends BottomSheetDialogFragment {
    View root;
    int tipe;
    Button btn_tambah;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Fragment parentFragment;
    private BottomSheetAddMakananListener mListener;
    EditText et_food_name, et_food_weight, et_food_calories;
    public BottomSheetAddFood(int tipe, Fragment parentFragment) {
    this.tipe = tipe;
    this.parentFragment = parentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.bottom_sheet_add_food, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initValue();
        initEvent();
    }

    private void initUI() {
        btn_tambah = root.findViewById(R.id.btn_tambah);
        et_food_name = root.findViewById(R.id.et_food_name);
        et_food_weight = root.findViewById(R.id.et_food_weight);
        et_food_calories = root.findViewById(R.id.et_food_calories);
    }

    private void initValue() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(tipe==5){
            et_food_weight.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_ml), null);
        }
    }

    private void initEvent() {
        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                    tambah();
            }
        });
    }

    private boolean validate() {
        return (!Validate.cek(et_food_name)&&!Validate.cek(et_food_weight)&&!Validate.cek(et_food_calories));
    }
    public interface BottomSheetAddMakananListener{
        void onAddFood(Map<String, Object> data_value);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetAddMakananListener) parentFragment;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Error");
        }
    }

    private void tambah() {
        Map<String, Object> data = new HashMap<>();
        data.put("date_added", Timestamp.now());
        data.put("food_calories", Integer.parseInt(et_food_calories.getText().toString()));
        data.put("food_name", et_food_name.getText().toString());
        data.put("food_weight", Integer.parseInt(et_food_weight.getText().toString()));
        Map<String, Object> data_value = new HashMap<>();
        if(tipe == 1){
            data_value.put("breakfast", FieldValue.arrayUnion(data));
        } else if(tipe == 2){
            data_value.put("lunch", FieldValue.arrayUnion(data));
        } else if(tipe == 3){
            data_value.put("dinner", FieldValue.arrayUnion(data));
        } else if(tipe == 4){
            data_value.put("snack", FieldValue.arrayUnion(data));
        } else {
            data_value.put("drink", FieldValue.arrayUnion(data));
        }
        mListener.onAddFood(data_value);
        dismiss();
    }
}
