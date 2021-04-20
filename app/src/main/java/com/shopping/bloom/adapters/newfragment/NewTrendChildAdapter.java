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
import com.shopping.bloom.model.fragmentnew.Child;

import java.util.List;

public class NewTrendChildAdapter extends RecyclerView.Adapter<NewTrendChildAdapter.ChildViewHolder> {

    private Context context;
    private List<Child> childList;

    public NewTrendChildAdapter(Context context, List<Child> childList) {
        this.context = context;
        this.childList = childList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChildViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_trend_child
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {

        holder.imageView.setBackgroundResource(childList.get(position).getBackground());
        holder.textView.setText(String.valueOf(childList.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return childList.size();
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
