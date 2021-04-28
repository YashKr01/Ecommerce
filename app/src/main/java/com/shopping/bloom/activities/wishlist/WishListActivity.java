package com.shopping.bloom.activities.wishlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shopping.bloom.activities.MainActivity;
import com.shopping.bloom.adapters.wishlist.WishListActivityAdapter;
import com.shopping.bloom.databinding.ActivityWishListBinding;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListActivity extends AppCompatActivity implements WishListProductListener {

    private ActivityWishListBinding binding;
    private List<WishListData> list;
    private WishListActivityAdapter adapter;
    private WishListViewModel viewModel;
    private String PAGE = "0", LIMIT = "10";
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //view model
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);

        // setup toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("WishList");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // setup list,adapter,recyclerview
        list = new ArrayList<>();
        adapter = new WishListActivityAdapter(this, list, this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);

        getWishList();

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (binding.recyclerView.canScrollVertically(1)) {
                    LIMIT = (Integer.parseInt(LIMIT) + 10 + "");
                    getPaginatedList();
                }
            }
        });

    }

    public void getWishList() {

        binding.progressBar.setVisibility(View.VISIBLE);

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

        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    public void getPaginatedList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getWishList(PAGE, LIMIT).observe(this, new Observer<List<WishListData>>() {
            @Override
            public void onChanged(List<WishListData> wishListData) {
                if (wishListData != null && wishListData.size() > 0) {
                    adapter.addAll(wishListData);
                }
            }
        });

        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    private void postRemaining() {
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.postRemainingItems();
        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    public void deleteItem(String id) {
        viewModel.deleteWishListProduct(id);
    }

    @Override
    public void wishListItemDelete(WishListData wishListData, int position) {

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Do yo want to delete this product from Wish List ?");

        builder.setPositiveButton("YES", (dialog, which) -> {

            deleteItem(String.valueOf(wishListData.getId()));
            list.remove(position);
            adapter.notifyItemRemoved(position);
            if (list.isEmpty()) {
                binding.txtEmptyWishlist.setVisibility(View.VISIBLE);
            }

        });

        builder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        postRemaining();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WishListActivity.this, MainActivity.class));
        finish();
    }

}