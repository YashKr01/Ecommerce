package com.shopping.bloom.adapters.coupons;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.coupons.Coupon;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class ExpiredCouponAdapter extends RecyclerView.Adapter<ExpiredCouponAdapter.ItemViewHolder> {

    private final List<Coupon> list;
    private final Context context;

    public ExpiredCouponAdapter(List<Coupon> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_expired_coupon,
                parent,
                false
        ));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Coupon currentItem = list.get(position);

        /*
         * if type is percentage display "50.00% OFF"
         * else display "FLAT 50.00 OFF"
         */
        if (currentItem.getType().equals("percentage")) {
            holder.rate.setText(currentItem.getDiscount() + "% OFF");
        } else {
            String discountRate = CommonUtils.getSignedAmount(String.valueOf(currentItem.getDiscount()));
            holder.rate.setText("FLAT " + discountRate + " OFF");
        }

        holder.date.setText(currentItem.getDate().substring(0, 10));
        holder.products.setText("For All Products");
        holder.limit.setText("For orders over " + currentItem.getMinimumAmount());
        holder.code.setText("CODE : " + currentItem.getPromoCode());
        holder.status.setText("Expires Soon");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView status, rate, date, code, limit, products;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.txt_expired_coupon_status);
            rate = itemView.findViewById(R.id.txt_expired_coupon_rate);
            date = itemView.findViewById(R.id.txt_expired_coupon_date);
            limit = itemView.findViewById(R.id.txt_expired_coupon_limit);
            code = itemView.findViewById(R.id.txt_expired_coupon_code);
            products = itemView.findViewById(R.id.txt_expired_coupon_products);
        }
    }
}

