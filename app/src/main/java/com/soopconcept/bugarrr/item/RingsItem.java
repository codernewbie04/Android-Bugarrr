package com.soopconcept.bugarrr.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.AktivitasModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RingsItem extends FirestoreRecyclerAdapter<AktivitasModel, RingsItem.RingsHolder> {
    int cal, exe, stands;
    public RingsItem(@NonNull FirestoreRecyclerOptions<AktivitasModel> options, int calories, int exercise, int stand) {
        super(options);
        this.cal = calories;
        this.exe = exercise;
        this.stands = stand;
    }

    @Override
    protected void onBindViewHolder(@NonNull RingsHolder holder, int position, @NonNull AktivitasModel model) {
        setupProgressBar(holder);
        holder.prog_kalori.setProgress(model.getCalories());
        holder.prog_olahraga.setProgress(model.getExercise());
        holder.prog_gerak.setProgress(model.getStands());

        holder.tv_kalori.setText(model.getCalories() + " Kcal");
        holder.tv_olahraga.setText(model.getExercise() + " Menit");
        holder.tv_gerak.setText(model.getStands() + " Jam");

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

    private void setupProgressBar(RingsHolder holder) {
        holder.prog_kalori.setMax(cal);
        holder.prog_olahraga.setMax(exe);
        holder.prog_gerak.setMax(stands);
    }

    @NonNull
    @Override
    public RingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rings, parent, false);
        return new RingsHolder(v);
    }

    public class RingsHolder extends RecyclerView.ViewHolder{
        ProgressBar prog_kalori, prog_olahraga, prog_gerak;
        TextView tv_kalori, tv_olahraga, tv_gerak, tanggal_sekarang;
        public RingsHolder(@NonNull View itemView) {
            super(itemView);
            prog_kalori = itemView.findViewById(R.id.prog_kalori);
            prog_olahraga = itemView.findViewById(R.id.prog_olahraga);
            prog_gerak = itemView.findViewById(R.id.prog_gerak);

            tv_kalori = itemView.findViewById(R.id.tv_kalori);
            tv_olahraga = itemView.findViewById(R.id.tv_olahraga);
            tv_gerak = itemView.findViewById(R.id.tv_gerak);

            tanggal_sekarang = itemView.findViewById(R.id.tanggal_sekarang);
        }
    }
}
