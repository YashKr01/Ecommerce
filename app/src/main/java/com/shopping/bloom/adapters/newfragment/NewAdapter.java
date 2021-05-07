package com.shopping.bloom.adapters.newfragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.newfragment.NewProduct;
import com.shopping.bloom.model.newfragment.NewProductCategory;
import com.shopping.bloom.restService.callback.NewProductOnClick;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyViewHolder> {

    private Context context;
    private List<NewProductCategory> list;
    private NewProductOnClick listener;

    public NewAdapter(Context context, List<NewProductCategory> list, NewProductOnClick listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_container_new, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NewProductCategory currentItem = list.get(position);

        holder.title.setText(currentItem.getCategoryName());
        holder.description.setText(currentItem.getType());

        String imagePath = "http://bloomapp.in" + currentItem.getThumbNail();
        CommonUtils.loadImageWithGlide(context,
                imagePath,
                holder.imageView,
                true);

        if (currentItem.getNewProductList() == null || currentItem.getNewProductList().isEmpty()) {
            holder.recyclerView.setVisibility(View.GONE);
        } else {
            setChildRecyclerView(holder.recyclerView, currentItem.getNewProductList(), listener);
        }


        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.newBannerListener(currentItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, description;
        RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.background_image);
            title = itemView.findViewById(R.id.new_text_view);
            description = itemView.findViewById(R.id.new_text_description);
            recyclerView = itemView.findViewById(R.id.child_order_recycler_view);
        }

    }

    private void setChildRecyclerView(RecyclerView recyclerView, List<NewProduct> productList, NewProductOnClick listener) {

        NewProductAdapter newProductAdapter = new NewProductAdapter(context, productList, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        ));

        recyclerView.setAdapter(newProductAdapter);
    }

}
