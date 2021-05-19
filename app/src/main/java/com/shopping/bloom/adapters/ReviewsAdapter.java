package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.review.Review;

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
        if (currentItem.getUserInfo() != null) {
            String name = currentItem.getUserInfo().getName().toLowerCase().substring(0, 1)
                    + "******" +
                    currentItem.getUserInfo().getName().toLowerCase()
                            .substring(currentItem.getUserInfo().getName().length() - 1);
            holder.itemName.setText(name);
        } else {
            // because before testing name was not in response, to handle those cases
            holder.itemName.setText("U******n");
        }

        holder.itemReview.setText(currentItem.getReview());
        holder.reviewRating.setRating(Float.parseFloat(currentItem.getRating()));

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemReview;
        RatingBar reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.item_review_name);
            reviewRating = itemView.findViewById(R.id.item_rating);
            itemReview = itemView.findViewById(R.id.item_review);

        }
    }
}
