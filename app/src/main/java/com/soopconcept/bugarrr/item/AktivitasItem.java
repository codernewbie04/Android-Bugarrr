package com.soopconcept.bugarrr.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.AktivitasModel;
import com.soopconcept.bugarrr.models.WorkoutsModel;

public class AktivitasItem extends RecyclerView.Adapter<AktivitasItem.AktivitasHolder>{
    Context mContext;
    WorkoutsModel data;

    public AktivitasItem(Context context, WorkoutsModel model)
    {
        this.mContext = context;
        this.data = model;
    }

    @NonNull
    @Override
    public AktivitasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktivitas, parent, false);
        return new AktivitasHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AktivitasHolder holder, int position) {
        holder.burned.setText("+ "+data.getData().get(position).getCalories_burned()+" Kcal");
        int selisih = (int) (data.getData().get(position).getEnd_time().getSeconds() - data.getData().get(position).getStart_time().getSeconds());
        if(selisih % 60 == 0 ){
            holder.waktu.setText(selisih/60 +" Menit");
        } else {
            holder.waktu.setText(selisih/60 +" Menit "+ selisih%60 + " Detik");
        }
        holder.workouts_name.setText(data.getData().get(position).getWorkout_name());
    }

    @Override
    public int getItemCount() {
        return (null != data.getData() ? data.getData().size() : 0);
    }

    class AktivitasHolder extends RecyclerView.ViewHolder
    {
        TextView workouts_name, waktu, burned;
        public AktivitasHolder(@NonNull View itemView) {
            super(itemView);
            workouts_name = itemView.findViewById(R.id.workouts_name);
            waktu = itemView.findViewById(R.id.waktu);
            burned = itemView.findViewById(R.id.burned);
        }
    }

}
