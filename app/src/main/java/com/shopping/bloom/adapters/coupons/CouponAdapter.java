package com.shopping.bloom.adapters.coupons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.coupons.Coupon;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ItemViewHolder> {

    private List<Coupon> list;
    private Context context;

    public CouponAdapter(List<Coupon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_coupon,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Coupon currentItem = list.get(position);

        holder.code.setText(currentItem.getCode());
        holder.date.setText(currentItem.getDate());
        holder.limit.setText(currentItem.getOrders());
        holder.rate.setText(currentItem.getRate());
        holder.status.setText(currentItem.getStatus());
        holder.products.setText(currentItem.getProducts());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView status, rate, date, code, limit, products;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.txt_coupon_status);
            rate = itemView.findViewById(R.id.txt_coupon_rate);
            date = itemView.findViewById(R.id.txt_coupon_date);
            limit = itemView.findViewById(R.id.txt_coupon_limit);
            code = itemView.findViewById(R.id.txt_coupon_code);
            products = itemView.findViewById(R.id.txt_coupon_products);
        }
    }
}
