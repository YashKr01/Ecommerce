package com.shopping.bloom.fragment.reviewsfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.reviewfragment.ReviewsAdapter;
import com.shopping.bloom.databinding.FragmentReviewsBinding;
import com.shopping.bloom.model.review.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewList = new ArrayList<>();

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        setRating();

        setProgress();

        binding.fabAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getContext());
            }
        });

    }

    // show custom dialog
    private void showDialog(Context context) {

        Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_review_dialog, null);
        dialog.setContentView(view);

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    // setting rating and progress
    private void setRating() {
        binding.reviewRatingBar.setRating(Float.parseFloat(binding.txtRating.getText().toString()));
    }

    // setting progress bar
    private void setProgress() {
        binding.progressBarSmall.setProgress(Integer.parseInt(binding.txtValueSmall.getText().toString()
                .substring(0, binding.txtValueSmall.getText().toString().length() - 1)));
        binding.progressBarSize.setProgress(Integer.parseInt(binding.txtValueTrue.getText().toString()
                .substring(0, binding.txtValueTrue.getText().toString().length() - 1)));
        binding.progressBarLarge.setProgress(Integer.parseInt(binding.txtValueLarge.getText().toString()
                .substring(0, binding.txtValueLarge.getText().toString().length() - 1)));
    }

    // initialise recyclerview
    private void initRecyclerView() {
        getList();
        reviewsAdapter = new ReviewsAdapter(reviewList, getContext());
        binding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewsRecyclerView.setClipToPadding(false);
        binding.reviewsRecyclerView.setNestedScrollingEnabled(false);
        binding.reviewsRecyclerView.setAdapter(reviewsAdapter);
    }

    // adding mock data as of now
    private void getList() {
        reviewList.clear();
        reviewList.add(new Review("Sam", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 4));
        reviewList.add(new Review("Nathan", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 5));
        reviewList.add(new Review("Watson", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 2));
        reviewList.add(new Review("Nicholas", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 4));
        reviewList.add(new Review("Chris", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 5));
        reviewList.add(new Review("Tom", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 3));
        reviewList.add(new Review("Sam", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.", 3));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}