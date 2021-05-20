package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.adapters.RecentlyViewedActivityAdapter;
import com.shopping.bloom.adapters.RecommendationsAdapter;
import com.shopping.bloom.databinding.ActivityRecentlyViewedBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.callback.RecentlyViewedListener;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.RecentlyViewedViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.shopping.bloom.utils.Const.REQ_SINGLE_PRODUCT;

public class RecentlyViewedActivity extends AppCompatActivity implements RecentlyViewedListener, ProductClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = RecentlyViewedActivity.class.getName();
    private ActivityRecentlyViewedBinding binding;
    private RecentlyViewedViewModel recentlyViewedViewModel;
    private RecommendationsAdapter recommendationsAdapter;
    private RecentlyViewedActivityAdapter recentlyViewedAdapter;

    private int RECENT_PAGE = 0;
    private final int PAGE_LIMIT = 30;
    private int RECOMMEND_PAGE = 0;

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
        recentlyViewedViewModel = new ViewModelProvider(this).get(RecentlyViewedViewModel.class);
        setUpRecyclerView();

        // GET INITIAL LISTS
        getRecentlyViewedList();
        getRecommendedList();

        // PAGINATION
        binding.nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View v = binding.nestedScrollView.getChildAt(binding.nestedScrollView.getChildCount() - 1);
            int diff = (v.getBottom() - (binding.nestedScrollView.getHeight() +
                    binding.nestedScrollView.getScrollY()));

            if (diff == 0)
                if (!binding.rvRecommendation.canScrollVertically(1))
                    if (!IS_RECOMMENDED_LOADING && !IS_RECOMMENDED_LAST_PAGE) {
                        RECOMMEND_PAGE = (RECOMMEND_PAGE + 1);
                        getRecommendedList();
                    }

        });
        binding.swipe.setOnRefreshListener(this);
    }

    private void setUpRecyclerView() {
        // initialise lists,adapters,recycler views
        recentlyViewedAdapter = new RecentlyViewedActivityAdapter(this, this);
        recommendationsAdapter = new RecommendationsAdapter( this, this);

        binding.rvRecentlyView.setNestedScrollingEnabled(false);
        binding.rvRecentlyView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvRecentlyView.setAdapter(recentlyViewedAdapter);

        binding.rvRecommendation.setNestedScrollingEnabled(false);
        binding.rvRecommendation.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvRecommendation.setAdapter(recommendationsAdapter);
    }


    // GET RECENTLY VIEWED LIST
    private void getRecentlyViewedList() {
        binding.progressBar.setVisibility(View.VISIBLE);
        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
            binding.progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        recentlyViewedViewModel.getRecentlyViewedList(RECENT_PAGE, PAGE_LIMIT, recentProductCallBack);
    }

    private final ProductResponseListener recentProductCallBack = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            if (products != null && !products.isEmpty()) {
                binding.tvEmpty.setVisibility(View.GONE);
                recentlyViewedAdapter.updateList(products);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            //binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    };

    // GET recommended product list from server
    private void getRecommendedList() {
        if (!NetworkCheck.isConnect(this)) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            showNoConnectionLayout(true);
            return;
        }
        IS_RECOMMENDED_LOADING = true;
        binding.progressBar.setVisibility(View.VISIBLE);
        recentlyViewedViewModel.getRecommendationList(PAGE_LIMIT, RECOMMEND_PAGE, listener);
    }

    private final ProductResponseListener listener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            if (products != null && products.size() > 0) {
                recommendationsAdapter.updateList(products);
            } else {
                Log.d(TAG, "onSuccess: NULL response");
                IS_RECOMMENDED_LAST_PAGE = true;
            }

            IS_RECOMMENDED_LOADING = false;
            binding.progressBar.setVisibility(View.INVISIBLE);

            if(recommendationsAdapter.getItemCount() == 0){
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            if (errorCode == Const.SUCCESS) {
                IS_RECOMMENDED_LAST_PAGE = true;
            }
        }
    };

    // DISPLAY NO CONNECTION LAYOUT
    private void showNoConnectionLayout(boolean show) {
        if (show) binding.noConnectionLayout.setVisibility(View.VISIBLE);
        else binding.noConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void recentItemClick(RecentlyViewedItem item) {
        String CALLING_ACTIVITY = RecentlyViewedActivity.class.getName();
        String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
        Intent intent = new Intent(RecentlyViewedActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", item.getId());
        startActivityForResult(intent, REQ_SINGLE_PRODUCT);
    }


    @Override
    public void onRefresh() {
        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
        } else {
            showNoConnectionLayout(false);
            RECOMMEND_PAGE = 0;
            RECENT_PAGE = 0;
            recommendationsAdapter.clearList();
            recentlyViewedAdapter.clearList();
            getRecommendedList();
            getRecentlyViewedList();
        }
        binding.swipe.setRefreshing(false);
    }

    @Override
    public void onProductClicked(Product product) {
        String CALLING_ACTIVITY = RecentlyViewedActivity.class.getName();
        String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
        Intent intent = new Intent(RecentlyViewedActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra(ARG_CALLING_ACTIVITY, CALLING_ACTIVITY);
        startActivityForResult(intent, REQ_SINGLE_PRODUCT);
    }
}