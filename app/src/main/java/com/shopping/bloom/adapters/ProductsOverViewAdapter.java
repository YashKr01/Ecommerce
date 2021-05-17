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
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProductsOverViewAdapter extends RecyclerView.Adapter<ProductsOverViewAdapter.ProductOverViewAdapter> {
    private static final String TAG = "ProductsOverViewAdapter";

    List<CartItem> cartItemList;
    Context context;

    public ProductsOverViewAdapter(Context context, List<CartItem> list) {
        this.context = context;
        cartItemList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ProductOverViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_overview, parent, false);
        return new ProductOverViewAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductOverViewAdapter holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.setUpData(context, item);
        holder.imgProductImage.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: ");
        });
    }

    static class ProductOverViewAdapter extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        TextView tvProductName, tvSize, tvPrice;
        TextView tvQuantity;
        public ProductOverViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvSize = itemView.findViewById(R.id.tv_color_size);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
        }

        void setUpData(Context context, CartItem item) {
            CommonUtils.loadImageWithGlide(
                    context,
                    Const.GET_BASE_URL + item.getPrimaryImage(),
                    imgProductImage,
                    false
            );
            String quantity = "Qty: " + item.getQuantity();

            tvProductName.setText(item.getName());
            tvQuantity.setText(quantity);
            tvSize.setText(item.getSize());
            tvPrice.setText(CommonUtils.getSignedAmount(item.getProductPrice()));
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
