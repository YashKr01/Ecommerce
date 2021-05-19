package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.adapters.RecommendationsAdapter;
import com.shopping.bloom.adapters.WishListActivityAdapter;
import com.shopping.bloom.databinding.ActivityWishListBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.callback.WishListItemClickListener;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.WishListViewModel;

import java.util.ArrayList;
import java.util.List;

public class WishListActivity extends AppCompatActivity implements WishListItemClickListener, SwipeRefreshLayout.OnRefreshListener, ProductClickListener {
    private static final String TAG = WishListActivity.class.getName();

    private ActivityWishListBinding binding;

    private List<WishListData> list;
    private List<Product> recommendationsItemList;

    private WishListActivityAdapter adapter;
    private RecommendationsAdapter recommendationsAdapter;

    private WishListViewModel wishListViewModel;

    private int PAGE = 0, LIMIT = 30;

    private int RECOMMENDATION_LIMIT = 30, RECOMMENDATION_PAGE = 0;
    private boolean IS_LAST_PAGE = false, IS_LOADING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefresh.setOnRefreshListener(this);

        //view model
        wishListViewModel = new ViewModelProvider(this).get(WishListViewModel.class);

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
        adapter = new WishListActivityAdapter(this,  this);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        binding.recommendationRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recommendationRecyclerView.setAdapter(recommendationsAdapter);

        // getting list from server
        checkNewtWorkAndFetchData();
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
                        RECOMMENDATION_PAGE = RECOMMENDATION_PAGE + 1;
                        getRecommendedList();
                    }
                }
            }
        });
    }

    // GET wishlist from server
    private void checkNewtWorkAndFetchData() {
        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
            binding.progressBar6.setVisibility(View.INVISIBLE);
            return;
        }
        binding.progressBar6.setVisibility(View.VISIBLE);
        wishListViewModel.getWishList(PAGE, LIMIT, wishListCallBack);
    }

    private final ProductResponseListener wishListCallBack = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            Log.d(TAG, "onSuccess: " + products.toString());
            binding.progressBar6.setVisibility(View.INVISIBLE);
            if (products != null && products.size() > 0) {
                adapter.updateList(products);//TODO: change this ASAP [vj]
                if (list.isEmpty()) binding.txtWishlistEmpty.setVisibility(View.GONE);
            } else {
                binding.txtWishlistEmpty.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            Log.d(TAG, "onFailure: errorCode: " + errorCode + " message " + message);
        }
    };

    // GET recommended product list from server
    private void getRecommendedList() {
        if (!NetworkCheck.isConnect(this)) {
            binding.progressBar6.setVisibility(View.INVISIBLE);
            showNoConnectionLayout(true);
            return;
        }
        IS_LOADING = true;
        binding.progressBar6.setVisibility(View.VISIBLE);
        wishListViewModel.getRecommendationList(RECOMMENDATION_LIMIT, RECOMMENDATION_PAGE, listener);
    }

    private final ProductResponseListener listener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            if (products != null && products.size() > 0) {
                int oldListSize = recommendationsItemList.size();
                recommendationsItemList.addAll(products);
                recommendationsAdapter.notifyItemRangeChanged(oldListSize, recommendationsItemList.size());
            } else {
                IS_LAST_PAGE = true;
            }

            IS_LOADING = false;
            binding.progressBar6.setVisibility(View.INVISIBLE);

            if (recommendationsItemList.size() == 0)
                binding.txtRecommendedEmptyList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailure(int errorCode, String message) {
            if (errorCode == Const.SUCCESS) {
                IS_LAST_PAGE = true;
            }
        }
    };

    private void postRemaining() {
        wishListViewModel.postRemainingItems();
    }

    public void deleteItem(String id) {
        wishListViewModel.deleteWishListProduct(id);
    }

    @Override
    public void onItemClick(Product wishListItem) {
        Intent intent = new Intent(WishListActivity.this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", wishListItem.getId());
        startActivity(intent);
    }

    @Override
    public void onItemDelete(Product wishListItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Do yo want to delete this product from Wish List ?")
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("YES", (dialog, which) -> {
                    deleteItem(String.valueOf(wishListItem.getId()));
                    adapter.removeItem(position);
                    if (list.isEmpty()) binding.txtWishlistEmpty.setVisibility(View.VISIBLE);
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        postRemaining();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        if (NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(false);
            checkNewtWorkAndFetchData();
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
    public void onProductClicked(Product product) {
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        startActivity(intent);
    }
}