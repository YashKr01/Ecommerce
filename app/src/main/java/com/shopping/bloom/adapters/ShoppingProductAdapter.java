package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.restService.callback.SimpleClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingProductAdapter extends RecyclerView.Adapter<ShoppingProductAdapter.ShoppingProductViewHolder> {
    private static final String TAG = ShoppingProductAdapter.class.getName();

    List<CartItem> cartItemList;
    Context context;
    SimpleClickListener mListener;

    public ShoppingProductAdapter(Context context, SimpleClickListener productClickListener) {
        this.context = context;
        this.mListener = productClickListener;
        cartItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShoppingProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_order_screen_product, parent, false);
        return new ShoppingProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShoppingProductAdapter.ShoppingProductViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.setUpData(context, item);
        holder.imgProductImage.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: ");
            mListener.onClick();
        });
    }

    static class ShoppingProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        TextView tvQuantity;
        public ShoppingProductViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            tvQuantity = itemView.findViewById(R.id.tvItemQuantity);
        }

        void setUpData(Context context, CartItem item) {
            CommonUtils.loadImageWithGlide(
                    context,
                    Const.GET_CATEGORY_DATA + item.getPrimaryImage(),
                    imgProductImage,
                    false
            );
            String quantity = "x" + item.getQuantity();
            tvQuantity.setText(quantity);
        }

    }

    public void updateList(List<CartItem> list) {
        if(cartItemList == null) {
            cartItemList = new ArrayList<>(list);
        }
        cartItemList.clear();
        cartItemList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(cartItemList == null) return 0;
        return cartItemList.size();
    }

}
