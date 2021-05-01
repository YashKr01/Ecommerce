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
        return new ChildViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_product
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        NewProduct currentItem = list.get(position);

        String imagePath = "http://bloomapp.in" + currentItem.getImagePath();
        CommonUtils.loadImageWithGlide(context,
                imagePath,
                holder.imageView,
                true);

        holder.textView.setText(currentItem.getPrice());

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.newProductListener(currentItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
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
