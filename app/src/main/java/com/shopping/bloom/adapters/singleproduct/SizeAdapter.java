package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    Context context;
    List<String> sizeList;
    boolean clickable;
    int pos = -1;

    public SizeAdapter(Context context, List<String> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
    }

    public void setSizeList(List<String> sizeList, boolean clickable) {
        this.sizeList = sizeList;
        this.clickable = clickable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_size_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.ViewHolder holder, int position) {
        holder.button.setText(sizeList.get(position));
        if (clickable) {
//            holder.button.setOnClickListener(v -> {
//                pos = position;
//                notifyDataSetChanged();
//                if (context instanceof SingleProductActivity) {
//                    ((SingleProductActivity) context).setSizeCurrentItem(position);
//                }
//            });
            holder.button.setOnClickListener(v -> {
                if(holder.button.isChecked()){
                    if (context instanceof SingleProductActivity) {
                        ((SingleProductActivity) context).setSizeCurrentItem(position);
                    }
                    pos = position;
                }else{
                    if (context instanceof SingleProductActivity) {
                        ((SingleProductActivity) context).setSizeCurrentItem(-1);
                    }
                    pos = -1;
                }
                notifyDataSetChanged();
            });

        }
        if (pos == position) {
            holder.button.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_300));
            holder.button.setChecked(true);
        } else {
            holder.button.setBackgroundResource(R.drawable.rect_back_button);
            holder.button.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        if (this.sizeList != null) {
            return this.sizeList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.colorButton);
        }
    }
}
