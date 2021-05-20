package com.shopping.bloom.fragment;

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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ReviewsAdapter;
import com.shopping.bloom.databinding.FragmentReviewsBinding;
import com.shopping.bloom.model.review.PostReview;
import com.shopping.bloom.model.review.Review;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ReviewViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewsFragment extends Fragment {

    private FragmentReviewsBinding binding;
    private ReviewsAdapter adapter;
    private List<Review> reviewList;
    private ReviewViewModel viewModel;
    private String PRODUCT_ID;
    private boolean canGiveReview;
    private String LIMIT = "30";
    private String PAGE = "0";

    public ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            PRODUCT_ID = getArguments().getString("PRODUCT_ID", "1");
            canGiveReview = getArguments().getBoolean("canGiveReview", false);
        }

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
        binding.fabAddReview.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if(canGiveReview){
                    showDialog(getContext());
                }else{
                    Toast.makeText(getContext(), "Can't Post Review", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get initial list
        getReviewList(PRODUCT_ID, LIMIT, PAGE);
    }

    // fetch data
    private void getReviewList(String productId, String limit, String page) {

        binding.progressBar.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getReviews(productId, limit, page)
                    .observe(getViewLifecycleOwner(), reviewModel -> {
                        // if list is empty or is null
                        if (reviewModel == null) {
                            reviewList.clear();
                            binding.progressBar.setVisibility(View.GONE);
                        } else if (reviewModel.getData() == null || reviewModel.getData().isEmpty()) {
                            binding.txtEmptyList.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                        } else {
                            // if obtained list is not empty
                            reviewList.clear();
                            reviewList.addAll(reviewModel.getData());
                            adapter.notifyDataSetChanged();
                            binding.txtEmptyList.setVisibility(View.GONE);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            //if connection is not enabled
            reviewList.clear();
            binding.progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
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

        // access views
        TextView submit = view.findViewById(R.id.dialog_submit);
        TextView cancel = view.findViewById(R.id.dialog_cancel);
        EditText textReview = view.findViewById(R.id.edittext_review);
        RatingBar ratingBar = view.findViewById(R.id.dialog_rating_bar);

        // cancel dialog box
        cancel.setOnClickListener(v -> dialog.cancel());

        // submit review
        submit.setOnClickListener(v -> {

            String review = textReview.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (review.isEmpty()) {
                Toast.makeText(context, "cannot post empty review", Toast.LENGTH_SHORT).show();
            } else if (rating == 0.0) {
                Toast.makeText(context, "please give rating", Toast.LENGTH_SHORT).show();
            } else {
                PostReview postReview = new PostReview(PRODUCT_ID, review, String.valueOf(rating));
                postProductReview(postReview);

                dialog.cancel();
                getReviewList(PRODUCT_ID, LIMIT, PAGE);
            }

        });

    }

    private void postProductReview(PostReview postReview) {
        viewModel.postReview(postReview);
    }

    // setting rating and progress

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}