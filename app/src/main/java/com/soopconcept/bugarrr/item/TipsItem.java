package com.soopconcept.bugarrr.item;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.activity.DetailTipsAct;
import com.soopconcept.bugarrr.models.TipsModel;
import com.soopconcept.bugarrr.utils.DateConvert;
import com.soopconcept.bugarrr.utils.PicassoTrustAll;

public class TipsItem extends FirestoreRecyclerAdapter<TipsModel, TipsItem.TipsHolder>{

    public TipsItem(@NonNull FirestoreRecyclerOptions<TipsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final TipsHolder holder, int position, @NonNull final TipsModel model) {
        holder.title.setText(model.getTitle());
        PicassoTrustAll.getInstance(holder.mContext)
                .load(model.getImage())
                .resize(250, 250)
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.mContext, DetailTipsAct.class);
                i.putExtra("title", model.getTitle());
                i.putExtra("content", model.getContent());
                i.putExtra("created_at", DateConvert.toDateString(model.getCreated_at()));
                i.putExtra("image", model.getImage());
                i.putExtra("subtitle", model.getSubtitle());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                holder.mContext.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public TipsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips, parent, false);
        return new TipsHolder(v);
    }

    public class TipsHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        Context mContext;
        public TipsHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            mContext = itemView.getContext();
        }
    }
}
