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
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.restService.callback.ShoppingBagItemListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBagAdapter extends RecyclerView.Adapter<ShoppingBagAdapter.ItemViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private ShoppingBagItemListener mListener;

    public ShoppingBagAdapter(Context context, ShoppingBagItemListener listener) {
        this.context = context;
        this.mListener = listener;
        this.cartItemList = new ArrayList<>();
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
        CartItem cartItem = cartItemList.get(position);
        holder.setUpData(cartItem, context);

        holder.tvAddItem.setOnClickListener((v)->{
            if(!NetworkCheck.isConnect(context)){
                mListener.noInternet();
                return;
            }
            changeQuantity(position, cartItem, true);
        });
        holder.tvRemoveItem.setOnClickListener((v)->{
            if(!NetworkCheck.isConnect(context)){
                mListener.noInternet();
                return;
            }
            changeQuantity(position, cartItem, false);
        });
    }

    private void changeQuantity(int position, CartItem cartItem, boolean increase) {
        if(increase) {
            if(cartItem.getQuantity() < cartItem.getMaxQuantity()) {
                cartItem.setQuantity(cartItem.getQuantity()+1);
                mListener.updateCartItem(cartItem);
            } else {
                mListener.maxItemAdded();
            }
        } else {
            if(cartItem.getQuantity() == 1) {
                mListener.removeCartItem(cartItem);
            } else {
                cartItem.setQuantity(cartItem.getQuantity()-1);
                mListener.updateCartItem(cartItem);
            }
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        TextView tvName, tvPrice, tvColorSize, txtQuantity;
        TextView tvAddItem, tvRemoveItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.img_shopping_bag);
            tvName = itemView.findViewById(R.id.txt_shopping_bag_product_name);
            tvPrice = itemView.findViewById(R.id.txt_shopping_bag_price);
            tvColorSize = itemView.findViewById(R.id.tv_color_size);
            txtQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvAddItem = itemView.findViewById(R.id.tvAddItem);
            tvRemoveItem = itemView.findViewById(R.id.tvRemoveItem);
        }

        public void setUpData(CartItem item, Context context) {
            CommonUtils.loadImageWithGlide(
                    context,
                    Const.GET_BASE_URL + item.getPrimaryImage(),
                    imgProductImage,
                    false
            );

            String colorSize = item.getColor() + ",  " + item.getSize();
            String qty = String.valueOf(item.getQuantity());
            tvColorSize.setText(colorSize);
            tvPrice.setText(CommonUtils.getSignedAmount(item.getProductPrice()));
            tvName.setText(item.getName());
            txtQuantity.setText(qty);
        }
    }

    public void updateList(List<CartItem> cartItems){
        if(cartItemList == null) {
            cartItemList = new ArrayList<>(cartItems);
        } else {
            cartItemList.clear();
            cartItemList.addAll(cartItems);
        }
        notifyDataSetChanged();
    }

    public void clearAll() {
        cartItemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
