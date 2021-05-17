package com.shopping.bloom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.shopping.bloom.adapters.wishlist.RecommendationsAdapter;
import com.shopping.bloom.adapters.wishlist.WishListActivityAdapter;
import com.shopping.bloom.databinding.ActivityWishListBinding;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationItem;
import com.shopping.bloom.restService.callback.RecommendationProductClickListener;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.recommendation.RecommendationViewModel;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity implements WishListProductListener, SwipeRefreshLayout.OnRefreshListener, RecommendationProductClickListener {

    private ActivityWishListBinding binding;

    private List<WishListData> list;
    private List<RecommendationItem> recommendationsItemList;

    private WishListActivityAdapter adapter;
    private RecommendationsAdapter recommendationsAdapter;

    private WishListViewModel viewModel;
    private RecommendationViewModel recommendationViewModel;

    private String PAGE = "0", LIMIT = "30";
    private AlertDialog.Builder builder;

    private String RECOMMENDATION_LIMIT = "30", RECOMMENDATION_PAGE = "0";
    private boolean IS_LAST_PAGE = false, IS_LOADING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefresh.setOnRefreshListener(this);

        //view model
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);
        recommendationViewModel = new ViewModelProvider(this).get(RecommendationViewModel.class);

        // setup toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("WishList");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
        }

        // setup list,adapter,recyclerview
        list = new ArrayList<>();
        recommendationsItemList = new ArrayList<>();
        recommendationsAdapter = new RecommendationsAdapter(recommendationsItemList, this, this);
        adapter = new WishListActivityAdapter(this, list, this);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        binding.recommendationRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recommendationRecyclerView.setAdapter(recommendationsAdapter);

        // getting list from server
        getWishList();
        getRecommendedList();

        // scrollview pagination (recommendation list)
        binding.nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

            View view = binding.nestedScrollView.getChildAt(binding.nestedScrollView.getChildCount() - 1);
            int diff = (view.getBottom() - (binding.nestedScrollView.getHeight() + binding.nestedScrollView
                    .getScrollY()));

            if (diff == 0) {
                if (!binding.recommendationRecyclerView.canScrollVertically(1)) {
                    if (!IS_LAST_PAGE && !IS_LOADING) {
                        // increasing page number
                        RECOMMENDATION_PAGE = String.valueOf(Integer.parseInt(RECOMMENDATION_PAGE) + 1);
                        getRecommendedList();
                    }
                }
            }

        });

    }

    // GET wishlist from server
    private void getWishList() {

        binding.progressBar6.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            viewModel.getWishList(PAGE, LIMIT).observe(this, wishListData -> {
                if (wishListData != null && wishListData.size() > 0) {
                    list.clear();
                    list.addAll(wishListData);
                    adapter.notifyDataSetChanged();
                    if (list.isEmpty()) binding.txtWishlistEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.txtWishlistEmpty.setVisibility(View.VISIBLE);
                }

                binding.progressBar6.setVisibility(View.INVISIBLE);
            });

        } else {
            showNoConnectionLayout(true);
            binding.progressBar6.setVisibility(View.INVISIBLE);
        }


    }

    // GET recommended product list from server
    private void getRecommendedList() {

        IS_LOADING = true;
        binding.progressBar6.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            recommendationViewModel.getRecommendationList(RECOMMENDATION_LIMIT, RECOMMENDATION_PAGE).observe(this, recommendationItems -> {
                if (recommendationItems != null && recommendationItems.size() > 0) {
                    int oldListSize = recommendationsItemList.size();
                    recommendationsItemList.addAll(recommendationItems);
                    recommendationsAdapter.notifyItemRangeChanged(oldListSize, recommendationsItemList.size());
                } else {
                    IS_LAST_PAGE = true;
                }

                IS_LOADING = false;
                binding.progressBar6.setVisibility(View.INVISIBLE);

                if (recommendationsItemList.size() == 0)
                    binding.txtRecommendedEmptyList.setVisibility(View.VISIBLE);

            });
        } else {
            binding.progressBar6.setVisibility(View.INVISIBLE);
            showNoConnectionLayout(true);
        }

    }

    private void postRemaining() {
        viewModel.postRemainingItems();
    }

    public void deleteItem(String id) {
        viewModel.deleteWishListProduct(id);
    }

    @Override
    public void wishListItemDelete(WishListData wishListData, int position) {

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Do yo want to delete this product from Wish List ?");
        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        builder.setPositiveButton("YES", (dialog, which) -> {

            deleteItem(String.valueOf(wishListData.getId()));
            list.remove(wishListData);
            adapter.notifyDataSetChanged();
            if (list.isEmpty()) binding.txtWishlistEmpty.setVisibility(View.VISIBLE);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void wishListItemCLicked(WishListData wishListData) {
        Intent intent = new Intent(WishListActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", wishListData.getId());
        startActivity(intent);
    }

    @Override
    public void recentlyViewedOnClicked(RecentlyViewedItem recentlyViewedItem) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        postRemaining();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "");
        setResult(123, intent);
        finish();
    }

    @Override
    public void onRefresh() {

        if (NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(false);
            getWishList();
        } else {
            showNoConnectionLayout(true);
        }

        binding.swipeRefresh.setRefreshing(false);
    }

    private void showNoConnectionLayout(boolean show) {
        if (show) binding.noConnectionLayout.setVisibility(View.VISIBLE);
        else binding.noConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void recommendationProductClickListener(RecommendationItem item) {
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", item.getId());
        startActivity(intent);
    }

}