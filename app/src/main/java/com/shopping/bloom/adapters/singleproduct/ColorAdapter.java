package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AddressAdapter;
import com.shopping.bloom.model.AddressDataResponse;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    List<String> colorList;

    public ColorAdapter(Context context, List<String> colorList) {
        this.context = context;
        this.colorList = colorList;
    }

    public void setColorList(List<String> colorList) {
        this.colorList = colorList;
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
