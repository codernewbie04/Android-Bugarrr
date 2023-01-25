package com.soopconcept.bugarrr.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.FoodModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AsupanItem  extends FirestoreRecyclerAdapter<FoodModel, AsupanItem.AsupanHolder> {
    public AsupanItem(@NonNull FirestoreRecyclerOptions<FoodModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AsupanHolder holder, int position, @NonNull FoodModel model) {
        int sum_cairan = 0, sum_makanan = 0;
        for(int i = 0; model.getDrink().size() > i; i++){
            sum_cairan = sum_cairan + model.getDrink().get(i).getFood_weight();
        }

        for(int i = 0; model.getBreakfast().size() > i; i++){
            sum_makanan = sum_makanan + model.getBreakfast().get(i).getFood_calories();
        }
        for(int i = 0; model.getLunch().size() > i; i++){
            sum_makanan = sum_makanan + model.getLunch().get(i).getFood_calories();
        }
        for(int i = 0; model.getDinner().size() > i; i++){
            sum_makanan = sum_makanan + model.getDinner().get(i).getFood_calories();
        }
        for(int i = 0; model.getSnack().size() > i; i++){
            sum_makanan = sum_makanan + model.getSnack().get(i).getFood_calories();
        }
        holder.total_kalori.setText(sum_makanan+" Kcal");
        holder.total_cairan.setText(sum_cairan+" mL");

        String documentId = getSnapshots().getSnapshot(position).getId();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM");
        SimpleDateFormat convertFormat = new SimpleDateFormat("dd-MM-YYYY");
        String date_formated;
        try {
            long date = new SimpleDateFormat("dd-MM-yyyy").parse(documentId).getTime();
            date_formated = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            date_formated = documentId;
        }
        holder.tanggal_sekarang.setText(date_formated);
    }

    @NonNull
    @Override
    public AsupanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asupan, parent, false);
        return new AsupanHolder(v);
    }

    public class AsupanHolder extends RecyclerView.ViewHolder
    {
        TextView tanggal_sekarang, total_kalori, total_cairan;
        public AsupanHolder(@NonNull View itemView) {
            super(itemView);
            tanggal_sekarang = itemView.findViewById(R.id.tanggal_sekarang);
            total_cairan = itemView.findViewById(R.id.total_cairan);
            total_kalori = itemView.findViewById(R.id.total_kalori);
        }
    }
}
