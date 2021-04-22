package com.shopping.bloom.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private Context context;

    public ReviewsAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_review,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        Review currentItem = reviewList.get(position);

        // display name in format Lucknow = l******w
        String name = currentItem.getName().toLowerCase().substring(0, 1)
                + "******" +
                currentItem.getName().toLowerCase().substring(currentItem.getName().length() - 1);

        holder.itemName.setText(name);
        holder.itemReview.setText(currentItem.getReview());
        holder.reviewRating.setRating(currentItem.getRating());

        // TODO : One Test Case still left when text is not ellipsized
        // setting read more read less at text review
        holder.txtReadMore.setOnClickListener(v -> {

            if (holder.txtReadMore.getText().toString().equals("Read More")) {
                holder.itemReview.setMaxLines(Integer.MAX_VALUE);
                holder.itemReview.setEllipsize(null);
                holder.txtReadMore.setText(R.string.read_less);
            } else {
                holder.itemReview.setMaxLines(4);
                holder.itemReview.setEllipsize(TextUtils.TruncateAt.END);
                holder.txtReadMore.setText(R.string.read_more);
            }

        });


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemReview, txtReadMore;
        RatingBar reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_review_name);
            reviewRating = itemView.findViewById(R.id.item_rating);
            itemReview = itemView.findViewById(R.id.item_review);
            txtReadMore = itemView.findViewById(R.id.txt_read_more);

        }
    }
}
