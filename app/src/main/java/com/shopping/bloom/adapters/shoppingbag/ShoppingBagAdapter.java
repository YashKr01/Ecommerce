package com.shopping.bloom.adapters.shoppingbag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.shoppingbag.ProductEntity;
import com.shopping.bloom.restService.callback.ShoppingBagItemListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.List;

import static java.lang.Integer.parseInt;

public class ShoppingBagAdapter extends RecyclerView.Adapter<ShoppingBagAdapter.ItemViewHolder> {

    private Context context;
    private List<ProductEntity> list;
    private ShoppingBagItemListener listener;

    public ShoppingBagAdapter(Context context, List<ProductEntity> list, ShoppingBagItemListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_shopping_bag,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ProductEntity currentItem = list.get(position);

        holder.txtSize.setText("S(8)/");
        holder.txtPrice.setText(currentItem.getProductPrice());
        holder.txtName.setText(currentItem.getProductName());

        CommonUtils.loadImageWithGlide(
                context,
                Const.GET_CATEGORY_DATA + currentItem.getProductImage(),
                holder.imageView,
                false
        );

        holder.imageAdd.setOnClickListener(new DebouncedOnClickListener(300) {
            @Override
            public void onDebouncedClick(View v) {
                int currQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
                holder.txtQuantity.setText(String.valueOf(currQuantity + 1));
            }
        });

        holder.imageRemove.setOnClickListener(new DebouncedOnClickListener(300) {
            @Override
            public void onDebouncedClick(View v) {
                int currentQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
                if (currentQuantity == 1) {
                    listener.btnRemoveClickListener(currentItem);
                } else {
                    holder.txtQuantity.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, imageAdd, imageRemove;
        TextView txtName, txtPrice, txtSize, txtQuantity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_shopping_bag);
            txtName = itemView.findViewById(R.id.txt_shopping_bag_product_name);
            txtPrice = itemView.findViewById(R.id.txt_shopping_bag_price);
            txtSize = itemView.findViewById(R.id.txt_shopping_bag_size);
            txtQuantity = itemView.findViewById(R.id.txt_shopping_bag_quantity);
            imageAdd = itemView.findViewById(R.id.img_shopping_bag_add);
            imageRemove = itemView.findViewById(R.id.img_shopping_bag_remove);
        }
    }
}
