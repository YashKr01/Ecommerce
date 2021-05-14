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
import com.shopping.bloom.restService.callback.CouponClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class UnusedCouponAdapter extends RecyclerView.Adapter<UnusedCouponAdapter.ItemViewHolder> {

    private final List<Coupon> list;
    private final Context context;
    private CouponClickListener mListener;

    public UnusedCouponAdapter(List<Coupon> list, Context context, CouponClickListener listener) {
        this.list = list;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_unused_coupon,
                parent,
                false
        ));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Coupon coupon = list.get(position);
        holder.setUPData(coupon);

        holder.itemView.setOnClickListener((view ->{
            mListener.onCouponClickListener(coupon);
        }));
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

        void setUPData(Coupon coupon) {
            /*
             * if type is percentage display "50.00% OFF"
             * else display "FLAT 50.00 OFF"
             */
            if (coupon.getType().equals("percentage")) {
                rate.setText(coupon.getDiscount() + "% OFF");
            } else {
                String discountRate = CommonUtils.getSignedAmount(String.valueOf(coupon.getDiscount()));
                rate.setText("FLAT " + discountRate + " OFF");
            }

            date.setText(coupon.getDate().substring(0, 10));
            products.setText("For All Products");
            limit.setText("For orders over " + coupon.getMinimumAmount());
            code.setText("CODE : " + coupon.getPromoCode());
            status.setText("Expires Soon");
        }

    }
}
