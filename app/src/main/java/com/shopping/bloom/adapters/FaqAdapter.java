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
        return new FaqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqModel faqModel = faqList.get(position);
//        holder.textView.setText(faqModel.getQuestion());
//        holder.textView2.setText(faqModel.getSolution());
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
//                holder.textView3.setVisibility(View.GONE);
//                holder.recyclerView.setVisibility(View.GONE);
            } else {
                holder.linearLayout.setVisibility(View.VISIBLE);
//                holder.textView3.setVisibility(View.VISIBLE);
//                holder.recyclerView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

//        holder.textView.setOnClickListener(v -> {
//            if (holder.textView2.getVisibility() == View.VISIBLE) {
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
//                holder.textView2.setVisibility(View.GONE);
//            } else {
//                holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
//                holder.textView2.setVisibility(View.VISIBLE);
//            }
//
//        });
//
//        if (holder.textView2.getVisibility() == View.VISIBLE) {
//            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
//        } else {
//            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
//        }
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //        TextView textView, textView2,
        TextView textView3;
        RecyclerView recyclerView;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.questionTextView);
//            textView2 = itemView.findViewById(R.id.solutionTextView);
            textView3 = itemView.findViewById(R.id.headerTextView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
