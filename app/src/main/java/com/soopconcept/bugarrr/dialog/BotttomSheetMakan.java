package com.soopconcept.bugarrr.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.soopconcept.bugarrr.R;

public class BotttomSheetMakan extends BottomSheetDialogFragment {
    View root;
    LinearLayout sarapan, makan_siang, makan_malam, cemilan, minum;
    Fragment this_fragment;

    public BotttomSheetMakan(Fragment this_fragment) {
        this.this_fragment = this_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.bottom_sheet_makan, container, false);
        initStart();
        return root;
    }

    private void initStart() {
        initUI();
        initEvent();
    }

    private void initUI() {
        sarapan = root.findViewById(R.id.sarapan);
        makan_siang = root.findViewById(R.id.makan_siang);
        makan_malam = root.findViewById(R.id.makan_malam);
        cemilan = root.findViewById(R.id.cemilan);
        minum = root.findViewById(R.id.minum);
    }

    private void initEvent() {
        sarapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddFood frag = new BottomSheetAddFood(1, this_fragment);
                frag.show(getChildFragmentManager(), "TambahBreakfast");
            }
        });

        makan_siang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddFood frag = new BottomSheetAddFood(2, this_fragment);
                frag.show(getChildFragmentManager(), "TambahLunch");
            }
        });

        makan_malam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddFood frag = new BottomSheetAddFood(3, this_fragment);
                frag.show(getChildFragmentManager(), "TambahDinner");
            }
        });

        cemilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddFood frag = new BottomSheetAddFood(4, this_fragment);
                frag.show(getChildFragmentManager(), "TambahSnack");
            }
        });

        minum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddFood frag = new BottomSheetAddFood(5, this_fragment);
                frag.show(getChildFragmentManager(), "TambahDrink");
            }
        });
    }
}
