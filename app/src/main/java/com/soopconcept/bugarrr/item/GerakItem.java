package com.soopconcept.bugarrr.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.utils.DateConvert;

import java.util.ArrayList;

public class GerakItem extends RecyclerView.Adapter<GerakItem.GerakHolder> {
    Context mContext;
    ArrayList<Timestamp> data;

    public GerakItem(Context context, ArrayList<Timestamp> model)
    {
        this.mContext = context;
        this.data = model;
    }

    @NonNull
    @Override
    public GerakHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktivitas, parent, false);
        return new GerakHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GerakHolder holder, int position) {
        holder.burned.setText("+ 1 Jam");
        holder.waktu.setText(DateConvert.getJam(data.get(position).getSeconds()));
        holder.workouts_name.setText("Gerak Campuran");
    }

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }

    class GerakHolder extends RecyclerView.ViewHolder{
        TextView workouts_name, waktu, burned;
        public GerakHolder(@NonNull View itemView) {
            super(itemView);
            workouts_name = itemView.findViewById(R.id.workouts_name);
            waktu = itemView.findViewById(R.id.waktu);
            burned = itemView.findViewById(R.id.burned);
        }
    }
}
