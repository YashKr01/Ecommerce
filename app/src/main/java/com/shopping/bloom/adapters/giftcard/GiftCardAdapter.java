package com.shopping.bloom.adapters.giftcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.giftcard.GiftCard;

import java.util.List;

public class GiftCardAdapter extends RecyclerView.Adapter<GiftCardAdapter.ItemViewHolder> {

    private List<GiftCard> list;
    private Context context;

    public GiftCardAdapter(List<GiftCard> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_gift_card,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        GiftCard currentItem = list.get(position);
        holder.amount.setText(currentItem.getAmount());
        holder.validity.setText(currentItem.getValidity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView amount, validity;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.txt_gift_card_amount);
            validity = itemView.findViewById(R.id.txt_gift_card_validity);
        }
    }
}
