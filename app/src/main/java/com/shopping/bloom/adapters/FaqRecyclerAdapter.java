package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.FaqModel;

import java.util.List;

public class FaqRecyclerAdapter extends RecyclerView.Adapter<FaqRecyclerAdapter.ViewHolder> {

    Context context;
    List<FaqModel> faqList;
    String title;

    public FaqRecyclerAdapter(Context context, List<FaqModel> faqList, String title) {
        this.context = context;
        this.faqList = faqList;
        this.title = title;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_faq, parent, false);
        return new FaqRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FaqModel faqModel = faqList.get(position);
        holder.textView.setText(faqModel.getQuestion());
        holder.textView2.setText(faqModel.getSolution());
        holder.cardView.setVisibility(View.VISIBLE);

//
        if (faqModel.getHeader().equals(title)) {
            holder.cardView.setVisibility(View.VISIBLE);
        }else{
            holder.cardView.setVisibility(View.GONE);
        }

        holder.textView.setOnClickListener(v -> {
            if (holder.textView2.getVisibility() == View.VISIBLE) {
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
                holder.textView2.setVisibility(View.GONE);
            } else {
                holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
                holder.textView2.setVisibility(View.VISIBLE);
            }

        });

        if (holder.textView2.getVisibility() == View.VISIBLE) {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
        } else {
            holder.textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
        }
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, textView2;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.questionTextView);
            textView2 = itemView.findViewById(R.id.solutionTextView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
