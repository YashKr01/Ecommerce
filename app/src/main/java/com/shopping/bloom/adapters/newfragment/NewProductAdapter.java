package com.shopping.bloom.adapters.newfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.newfragment.NewProduct;
import com.shopping.bloom.restService.callback.NewProductOnClick;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ChildViewHolder> {

    private Context context;
    private List<NewProduct> list;
    private NewProductOnClick listener;

    public NewProductAdapter(Context context, List<NewProduct> list, NewProductOnClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.item_see_more) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product, parent, false);
        }

        return new ChildViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size()) ? R.layout.item_see_more : R.layout.item_new_product;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        if (position == list.size()) {

        } else {
            NewProduct currentItem = list.get(position);

            CommonUtils.loadImageWithGlide(context,
                    Const.GET_BASE_URL + currentItem.getImagePath(),
                    holder.imageView,
                    true);
            holder.textView.setText(CommonUtils.getSignedAmount(currentItem.getPrice()));
            holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    listener.newProductListener(currentItem);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_product);
            textView = itemView.findViewById(R.id.txt_product_price);
        }
    }


}
