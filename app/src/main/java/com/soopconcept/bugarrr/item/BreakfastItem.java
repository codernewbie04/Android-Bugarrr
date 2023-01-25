package com.soopconcept.bugarrr.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soopconcept.bugarrr.R;
import com.soopconcept.bugarrr.models.DetailFood;
import com.soopconcept.bugarrr.models.FoodModel;
import com.soopconcept.bugarrr.utils.DateConvert;

public class BreakfastItem extends RecyclerView.Adapter<BreakfastItem.BreakfastHolder>{
    Context mContext;
    FoodModel food;
    int type;

    public BreakfastItem(Context context, FoodModel food, int type)
    {
        this.mContext = context;
        this.food = food;
        this.type = type;
    }

    @NonNull
    @Override
    public BreakfastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new BreakfastHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakfastHolder holder, int position) {
        if(type == 1){
            holder.food_name.setText(food.getBreakfast().get(position).getFood_name());
            holder.time.setText(DateConvert.getJam(food.getBreakfast().get(position).getDate_added().getSeconds()));
            holder.weight.setText("- "+ food.getBreakfast().get(position).getFood_calories() + " Kcal");
        } else if(type == 2){
            holder.food_name.setText(food.getLunch().get(position).getFood_name());
            holder.time.setText(DateConvert.getJam(food.getLunch().get(position).getDate_added().getSeconds()));
            holder.weight.setText("- "+ food.getLunch().get(position).getFood_calories() + " Kcal");
        } else if(type == 3){
            holder.food_name.setText(food.getDinner().get(position).getFood_name());
            holder.time.setText(DateConvert.getJam(food.getDinner().get(position).getDate_added().getSeconds()));
            holder.weight.setText("- "+ food.getDinner().get(position).getFood_calories() + " Kcal");
        } else if(type == 4){
            holder.food_name.setText(food.getSnack().get(position).getFood_name());
            holder.time.setText(DateConvert.getJam(food.getSnack().get(position).getDate_added().getSeconds()));
            holder.weight.setText("- "+ food.getSnack().get(position).getFood_calories() + " Kcal");
        } else {
            holder.food_name.setText(food.getDrink().get(position).getFood_name());
            holder.time.setText(DateConvert.getJam(food.getDrink().get(position).getDate_added().getSeconds()));
            holder.weight.setText("+ "+ food.getDrink().get(position).getFood_weight() + " ml");
        }
        if(type == 5){
            holder.title.setText("Minuman");
            holder.image.setImageResource(R.drawable.ic_minum);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(type == 1){
            count = (null != food ? food.getBreakfast().size() : 0);
        } else if(type == 2){
            count = (null != food ? food.getLunch().size() : 0);
        } else if(type == 3){
            count = (null != food ? food.getDinner().size() : 0);
        } else if(type == 4){
            count = (null != food ? food.getSnack().size() : 0);
        } else {
            count = (null != food ? food.getDrink().size() : 0);
        }
        return count;
    }

    class BreakfastHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, food_name, time, weight;
        public BreakfastHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            food_name = itemView.findViewById(R.id.food_name);
            time = itemView.findViewById(R.id.time);
            weight = itemView.findViewById(R.id.weight);
        }
    }
}
