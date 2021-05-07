package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    List<String> colorList;
    int pos = -1;
    boolean clickable;
    HashMap<String, String> colorMap;

    public ColorAdapter(Context context, List<String> colorList) {
        this.context = context;
        this.colorList = colorList;
        colorMap = new HashMap<>();
        fillColorHashMap();
    }

    public void setColorList(List<String> colorList, boolean clickable) {
        this.colorList = colorList;
        this.clickable = clickable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_color_button, parent, false);
        return new ViewHolder(view);
    }

    private void fillColorHashMap() {
        colorMap.put("Blue", "#2A93DF");
        colorMap.put("Red", "#FD5353");
        colorMap.put("Black", "#000000");
        colorMap.put("Brown", "#A0552B");
        colorMap.put("Green", "#11DF3E");
        colorMap.put("Yellow", "#FAE423");
        colorMap.put("White", "#E6E6E6");
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        String color = colorMap.get(colorList.get(position));
        System.out.println(color);
        if (color == null) {
            System.out.println("Empty color");
        } else {
            holder.button.setCardBackgroundColor(Color.parseColor(color));
        }

        if (clickable) {

            holder.cardView.setOnClickListener(v -> {
                pos = position;
                notifyDataSetChanged();
                if (context instanceof SingleProductActivity) {
                    ((SingleProductActivity) context).setViewPagerCurrentItem(position);
                }
            });

            if (pos == position) {
                holder.cardView.setContentPadding(10,10,10,10);
            } else {
                holder.cardView.setContentPadding(0,0,0,0);
            }

        }
    }

    @Override
    public int getItemCount() {
        if (this.colorList != null) {
            return this.colorList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView button;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.colorButton);
            cardView = itemView.findViewById(R.id.cardView);
        }

    }

}
