package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.SingleProductDescResponse;

import java.util.List;

public class ProductDescAdapter extends RecyclerView.Adapter<ProductDescAdapter.ViewHolder> {

    Context context;
    List<SingleProductDescResponse> singleProductDescResponseList;

    public ProductDescAdapter(Context context, List<SingleProductDescResponse> singleProductDescResponseList) {
        this.context = context;
        this.singleProductDescResponseList = singleProductDescResponseList;
    }

    public void setSingleProductDescResponseList(List<SingleProductDescResponse> singleProductDescResponseList) {
        this.singleProductDescResponseList = singleProductDescResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductDescAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_desc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDescAdapter.ViewHolder holder, int position) {
        holder.property_name.setText(singleProductDescResponseList.get(position).getProperty_name()+": ");
        holder.property_value.setText(singleProductDescResponseList.get(position).getProperty_value());
    }

    @Override
    public int getItemCount() {
        if (this.singleProductDescResponseList != null) {
            return this.singleProductDescResponseList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView property_name, property_value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            property_name = itemView.findViewById(R.id.property_name);
            property_value = itemView.findViewById(R.id.property_value);
        }
    }
}
