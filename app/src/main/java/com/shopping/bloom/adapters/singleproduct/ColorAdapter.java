package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    List<String> colorList;
    int pos = -1;
    boolean clickable;

    public ColorAdapter(Context context, List<String> colorList) {
        this.context = context;
        this.colorList = colorList;
    }

    public void setColorList(List<String> colorList, boolean clickable) {
        this.colorList = colorList;
        this.clickable = clickable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        holder.button.setText(colorList.get(position));
        if (clickable) {

            holder.button.setOnClickListener(v -> {
                pos = position;
                notifyDataSetChanged();
                if (context instanceof SingleProductActivity) {
                    ((SingleProductActivity) context).setViewPagerCurrentItem(position);
                }
            });

            if (pos == position) {
                holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_300));
            } else {
                holder.button.setBackgroundResource(R.drawable.rect_back_button);
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
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.colorButton);

        }

    }

}
