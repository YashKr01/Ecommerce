package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;

import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {

    Context context;
    List<String> sizeList;

    public SizeAdapter(Context context, List<String> sizeList) {
        this.context = context;
        this.sizeList = sizeList;
    }

    public void setSizeList(List<String> sizeList) {
        this.sizeList = sizeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeAdapter.ViewHolder holder, int position) {
        holder.button.setText(sizeList.get(position));
    }

    @Override
    public int getItemCount() {
        if (this.sizeList != null) {
            return this.sizeList.size();
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
