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
import com.shopping.bloom.model.fragmentnew.Child;
import com.shopping.bloom.model.fragmentnew.NewTrends;

import java.util.List;

public class NewTrendAdapter extends RecyclerView.Adapter<NewTrendAdapter.MyViewHolder> {

    private List<NewTrends> list;
    private Context context;

    public NewTrendAdapter(List<NewTrends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
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
        holder.setNewData(list.get(position), context);
        setChildRecyclerView(holder.recyclerView, list.get(position).getChildList());
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
            //CommonUtils.loadImageWithGlide(context, data.getImageUrl(), imageView, true);
            imageView.setBackgroundResource(data.getBackground());
            title.setText(data.getTitle());
            description.setText(data.getDescription());
        }

    }

    private void setChildRecyclerView(RecyclerView recyclerView, List<Child> childList) {

        NewTrendChildAdapter newTrendChildAdapter = new NewTrendChildAdapter(context, childList);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, RecyclerView.HORIZONTAL, false
        ));
        recyclerView.setAdapter(newTrendChildAdapter);


    }

}
