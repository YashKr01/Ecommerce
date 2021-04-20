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
import com.shopping.bloom.model.fragmentnew.NewTrends;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class NewTrendAdapter extends RecyclerView.Adapter<NewTrendAdapter.MyViewHolder> {

    private List<NewTrends> list;
    private Context context;

    public NewTrendAdapter(List<NewTrends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_container_new_trends, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewTrends currentItem = list.get(position);

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

        void setNewData(NewTrends data, Context context) {
            CommonUtils.loadImageWithGlide(context, data.getImageUrl(), imageView, true);
            imageView.setBackgroundResource(data.getBackground());
            title.setText(data.getTitle());
            description.setText(data.getDescription());
        }

    }

    private void setChildRecyclerView(RecyclerView recyclerView, List<Product> productList) {

        NewTrendProductAdapter newTrendProductAdapter = new NewTrendProductAdapter(context, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        ));
        recyclerView.setAdapter(newTrendProductAdapter);
    }

}
