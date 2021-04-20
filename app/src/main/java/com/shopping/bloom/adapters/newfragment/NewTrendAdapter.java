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
import com.shopping.bloom.model.fragmentnew.NewTrends;

import java.util.List;

public class NewTrendAdapter extends RecyclerView.Adapter<NewTrendAdapter.Holder1> {

    private List<NewTrends> list;
    private Context context;

    public NewTrendAdapter(List<NewTrends> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder1(LayoutInflater.from(context).inflate(
                R.layout.item_container_new_trends, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder1 holder, int position) {
        holder.setNewData(list.get(position), context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder1 extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, description;

        public Holder1(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.background_image);
            title = itemView.findViewById(R.id.new_text_view);
            description = itemView.findViewById(R.id.new_text_description);
        }

        void setNewData(NewTrends data, Context context) {
            //CommonUtils.loadImageWithGlide(context, data.getImageUrl(), imageView, true);
            imageView.setBackgroundColor(data.getBackground());
            title.setText(data.getTitle());
            description.setText(data.getDescription());
        }

    }

}
