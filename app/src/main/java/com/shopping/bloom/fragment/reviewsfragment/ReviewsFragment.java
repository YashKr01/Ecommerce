package com.shopping.bloom.fragment.reviewsfragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.reviewfragment.ReviewsAdapter;
import com.shopping.bloom.databinding.FragmentReviewsBinding;
import com.shopping.bloom.model.review.Review;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private ReviewsAdapter adapter;
    private List<Review> reviewList;
    private ReviewViewModel viewModel;
    private String PRODUCT_ID = "2";
    private String LIMIT = "10";
    private String PAGE = "0";

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);

        // initialise view model here
        viewModel = new ViewModelProvider(requireActivity()).get(ReviewViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup recyclerview and adapter
        reviewList = new ArrayList<>();
        adapter = new ReviewsAdapter(reviewList, getContext());
        binding.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.reviewsRecyclerView.setAdapter(adapter);

        // show custom dialog on floating action button click
        binding.fabAddReview.setOnClickListener(v -> showDialog(getContext()));

        //get initial list
        getReviewList(PRODUCT_ID, LIMIT, PAGE);

    }

    // fetch data using MVVM
    private void getReviewList(String productId, String limit, String page) {

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getReviews(productId, limit, page)
                    .observe(getViewLifecycleOwner(), reviewModel -> {
                        // if list is empty or is null
                        if (reviewModel.getData().isEmpty() || reviewModel.getData() == null) {
                            binding.txtEmptyList.setVisibility(View.VISIBLE);
                        } else {
                            // if obtained list is not empty
                            reviewList.clear();
                            reviewList.addAll(reviewModel.getData());
                            adapter.notifyDataSetChanged();
                            binding.txtEmptyList.setVisibility(View.GONE);
                        }
                    });
        } else {
            //if connection is not enabled
            reviewList.clear();
            Toast.makeText(getContext(), "An error Occured", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}