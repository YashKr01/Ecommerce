package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.adapters.recentlyviewedactivity.RecentlyViewedActivityAdapter;
import com.shopping.bloom.adapters.wishlist.RecommendationsAdapter;
import com.shopping.bloom.databinding.ActivityRecentlyViewedBinding;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationItem;
import com.shopping.bloom.restService.callback.RecentlyViewedListener;
import com.shopping.bloom.restService.callback.RecommendationProductClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.recentlyviewed.RecentlyViewedViewModel;
import com.shopping.bloom.viewModels.recommendation.RecommendationViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedActivity extends AppCompatActivity implements RecentlyViewedListener, RecommendationProductClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ActivityRecentlyViewedBinding binding;
    private List<RecentlyViewedItem> recentlyViewedList;
    private List<RecommendationItem> recommendationItemList;
    private RecommendationViewModel recommendationViewModel;
    private RecentlyViewedViewModel recentlyViewedViewModel;
    private RecommendationsAdapter recommendationsAdapter;
    private RecentlyViewedActivityAdapter recentlyViewedAdapter;

    private String RECENT_PAGE = "0", RECENT_LIMIT = "30";
    private String RECOMMEND_PAGE = "0", RECOMMEND_LIMIT = "30";

    private boolean IS_RECOMMENDED_LAST_PAGE = false, IS_RECOMMENDED_LOADING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentlyViewedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setup toolbar
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // swipe refresh
        binding.swipe.setOnRefreshListener(this);

        // initialise view model
        recommendationViewModel = new ViewModelProvider(this).get(RecommendationViewModel.class);
        recentlyViewedViewModel = new ViewModelProvider(this).get(RecentlyViewedViewModel.class);

        // initialise lists,adapters,recycler views
        recentlyViewedList = new ArrayList<>();
        recommendationItemList = new ArrayList<>();
        recentlyViewedAdapter = new RecentlyViewedActivityAdapter(recentlyViewedList, this, this);
        recommendationsAdapter = new RecommendationsAdapter(recommendationItemList, this, this);

        binding.recentlyViewedRecyclerView.setNestedScrollingEnabled(false);
        binding.recentlyViewedRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recentlyViewedRecyclerView.setAdapter(recentlyViewedAdapter);

        binding.recommendedRecyclerView.setNestedScrollingEnabled(false);
        binding.recommendedRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recommendedRecyclerView.setAdapter(recommendationsAdapter);

        // GET INITIAL LISTS
        getRecentlyViewedList();
        getRecommendedList();

        // LISTENER FOR PAGINATION
        binding.nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

            View v = binding.nestedScrollView.getChildAt(binding.nestedScrollView.getChildCount() - 1);
            int diff = (v.getBottom() - (binding.nestedScrollView.getHeight() +
                    binding.nestedScrollView.getScrollY()));

            if (diff == 0)
                if (!binding.recommendedRecyclerView.canScrollVertically(1))
                    if (!IS_RECOMMENDED_LOADING && !IS_RECOMMENDED_LAST_PAGE) {
                        RECOMMEND_PAGE = String.valueOf(Integer.parseInt(RECOMMEND_PAGE) + 1);
                        getRecommendedList();
                    }

        });


    }

    // GET RECENTLY VIEWED LIST
    private void getRecentlyViewedList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            recentlyViewedViewModel.getRecentlyViewedList(RECENT_PAGE, RECENT_LIMIT).observe(this, list -> {
                if (list != null && list.size() > 0) {
                    recentlyViewedList.addAll(list);
                    recentlyViewedAdapter.notifyDataSetChanged();
                }
                binding.progressBar.setVisibility(View.INVISIBLE);
            });
        } else {
            showNoConnectionLayout(true);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }


    }

    // GET recommended product list from server
    private void getRecommendedList() {

        IS_RECOMMENDED_LOADING = true;
        binding.progressBar.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            recommendationViewModel.getRecommendationList(RECOMMEND_LIMIT, RECOMMEND_PAGE).observe(this, list -> {

                if (list != null && list.size() > 0) {
                    int oldListSize = recommendationItemList.size();
                    recommendationItemList.addAll(list);
                    recommendationsAdapter.notifyItemRangeChanged(oldListSize, recommendationItemList.size());
                } else {
                    IS_RECOMMENDED_LAST_PAGE = true;
                }

                IS_RECOMMENDED_LOADING = false;
                binding.progressBar.setVisibility(View.INVISIBLE);

                if (recommendationItemList.size() == 0)
                    binding.txtEmptyList.setVisibility(View.VISIBLE);

            });
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            showNoConnectionLayout(true);
        }

    }

    // DISPLAY NO CONNECTION LAYOUT
    private void showNoConnectionLayout(boolean show) {
        if (show) binding.noConnectionLayout.setVisibility(View.VISIBLE);
        else binding.noConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void recentItemClick(RecentlyViewedItem item) {
        Intent intent = new Intent(RecentlyViewedActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", item.getId());
        startActivity(intent);
    }

    @Override
    public void recommendationProductClickListener(RecommendationItem item) {
        Intent intent = new Intent(RecentlyViewedActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", item.getId());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(false);

            RECOMMEND_PAGE = "0";
            RECENT_PAGE = "0";

            recommendationItemList.clear();
            recommendationsAdapter.notifyDataSetChanged();

            recentlyViewedList.clear();
            recentlyViewedAdapter.notifyDataSetChanged();

            getRecommendedList();
            getRecentlyViewedList();

        } else showNoConnectionLayout(true);

        binding.swipe.setRefreshing(false);
    }

}