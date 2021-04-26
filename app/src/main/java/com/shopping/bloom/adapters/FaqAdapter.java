package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.FaqModel;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    Context context;
    List<FaqModel> faqList;
    FaqRecyclerAdapter faqRecyclerAdapter;

    public FaqAdapter(Context context, List<FaqModel> faqList) {
        this.context = context;
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_recyclerview_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqModel faqModel = faqList.get(position);
        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.textView3.setText(faqModel.getHeader());
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        faqRecyclerAdapter = new FaqRecyclerAdapter(context, faqList, faqModel.getHeader());
        holder.recyclerView.setAdapter(faqRecyclerAdapter);
        String header = faqList.get(position).getHeader();

        try {
            String prevHeader = faqList.get(position - 1).getHeader();

            if (header.equals(prevHeader)) {
                holder.linearLayout.setVisibility(View.GONE);
            } else {
                holder.linearLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception ignored) {
        }

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView3;
        RecyclerView recyclerView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView3 = itemView.findViewById(R.id.headerTextView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
