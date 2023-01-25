package com.soopconcept.bugarrr.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.LatihanModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutsItem extends FirestoreRecyclerAdapter<WorkoutsModel, WorkoutsItem.WorkoutsHolder> {

    public WorkoutsItem(@NonNull FirestoreRecyclerOptions<WorkoutsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkoutsHolder holder, int position, @NonNull WorkoutsModel model) {
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
        holder.tanggal.setText(date_formated);

        if(model.getData().size() > 0){
            holder.workouts.setVisibility(View.VISIBLE);
            AktivitasItem adapter = new AktivitasItem(holder.mContext, model);
            holder.workouts.setAdapter(adapter);
        }

    }

    @NonNull
    @Override
    public WorkoutsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workouts, parent, false);
        return new WorkoutsHolder(v);
    }

    public class WorkoutsHolder extends RecyclerView.ViewHolder
    {
        RecyclerView workouts;
        TextView tanggal;
        Context mContext;
        public WorkoutsHolder(@NonNull View itemView) {
            super(itemView);
            tanggal = itemView.findViewById(R.id.tanggal);
            workouts = itemView.findViewById(R.id.workouts);
            mContext = itemView.getContext();
            workouts.setNestedScrollingEnabled(false);
            workouts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        }
    }
}
