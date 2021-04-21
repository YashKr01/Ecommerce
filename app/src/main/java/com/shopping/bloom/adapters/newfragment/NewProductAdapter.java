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
import com.shopping.bloom.model.Product;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ChildViewHolder> {

    private Context context;
    private List<Product> productList;

    public NewProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_product
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        // get current product list
        Product currentItem = productList.get(position);

        CommonUtils.loadImageWithGlide(context, currentItem.getBig_thumbnail()
                , holder.imageView, false);
        holder.textView.setText(currentItem.getType());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_child);
            textView = itemView.findViewById(R.id.txt_child_price);

        }
    }
}
