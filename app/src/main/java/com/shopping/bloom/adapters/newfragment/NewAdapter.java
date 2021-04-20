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
import com.shopping.bloom.model.fragmentnew.NewTrend;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyViewHolder> {

    private List<NewTrend> list;
    private Context context;

    public NewAdapter(List<NewTrend> list, Context context) {
        this.list = list;
        this.context = context;
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
        NewTrend currentItem = list.get(position);

        holder.setNewData(currentItem, context);
        setChildRecyclerView(holder.recyclerView, currentItem.getProductList());
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
            recyclerView = itemView.findViewById(R.id.child_recycler_view);
        }

        void setNewData(NewTrend data, Context context) {
            CommonUtils.loadImageWithGlide(context, data.getImageUrl(), imageView, false);
            imageView.setBackgroundResource(data.getBackground());
            title.setText(data.getTitle());
            description.setText(data.getDescription());
        }

    }

    private void setChildRecyclerView(RecyclerView recyclerView, List<Product> productList) {

        NewProductAdapter newProductAdapter = new NewProductAdapter(context, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        ));
        recyclerView.setAdapter(newProductAdapter);
    }

}
