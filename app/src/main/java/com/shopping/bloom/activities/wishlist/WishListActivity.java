package com.shopping.bloom.activities.wishlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.adapters.wishlist.RecommendationsAdapter;
import com.shopping.bloom.adapters.wishlist.WishListActivityAdapter;
import com.shopping.bloom.databinding.ActivityWishListBinding;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.model.wishlist.recommendations.RecommendationsItem;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListActivity extends AppCompatActivity implements WishListProductListener, SwipeRefreshLayout.OnRefreshListener {

    private ActivityWishListBinding binding;
    private List<WishListData> list;
    private List<RecommendationsItem> recommendationsItemList;
    private WishListActivityAdapter adapter;
    private RecommendationsAdapter recommendationsAdapter;
    private WishListViewModel viewModel;
    private String PAGE = "0", LIMIT = "10";
    private AlertDialog.Builder builder;

    private String IMAGE_URL = "http://bloomapp.in/images/product/product1619491000.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.swipeRefresh.setOnRefreshListener(this);

        //view model
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);

        // setup toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("WishList");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
        }

        // setup list,adapter,recyclerview
        list = new ArrayList<>();
        recommendationsItemList = new ArrayList<>();

        recommendationsAdapter = new RecommendationsAdapter(recommendationsItemList, this);
        adapter = new WishListActivityAdapter(this, list, this);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        binding.recommendationRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recommendationRecyclerView.setAdapter(recommendationsAdapter);
        binding.recommendationRecyclerView.setNestedScrollingEnabled(false);

        getWishList();

    }

    private List<RecommendationsItem> tempList() {

        List<RecommendationsItem> recommendationsItems = new ArrayList<>();
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));
        recommendationsItems.add(new RecommendationsItem(1, "Product 1", IMAGE_URL));

        return recommendationsItems;
    }

    public void getWishList() {

        if (NetworkCheck.isConnect(this)) {
            viewModel.getWishList(PAGE, LIMIT).observe(this, wishListData -> {
                if (wishListData != null && wishListData.size() > 0) {
                    list.clear();
                    list.addAll(wishListData);
                    adapter.notifyDataSetChanged();
                    binding.txtEmptyWishlist.setVisibility(View.INVISIBLE);
                } else {
                    binding.txtEmptyWishlist.setVisibility(View.VISIBLE);
                }
            });
            recommendationsItemList.addAll(tempList());
            adapter.notifyDataSetChanged();
        } else {
            binding.txtEmptyWishlist.setVisibility(View.INVISIBLE);
            showNoConnectionLayout(true);
        }


    }

    public void getPaginatedList() {

        viewModel.getWishList(PAGE, LIMIT).observe(this, new Observer<List<WishListData>>() {
            @Override
            public void onChanged(List<WishListData> wishListData) {
                if (wishListData != null && wishListData.size() > 0) {
                    adapter.addAll(wishListData);
                }
            }
        });

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
            list.remove(position);
            adapter.notifyItemRemoved(position);

            if (list.isEmpty()) binding.txtEmptyWishlist.setVisibility(View.VISIBLE);
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
        if (show) {
            binding.noConnectionLayout.setVisibility(View.VISIBLE);
        } else {
            binding.noConnectionLayout.setVisibility(View.GONE);
        }
    }


}