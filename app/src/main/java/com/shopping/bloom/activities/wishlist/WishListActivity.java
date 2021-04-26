package com.shopping.bloom.activities.wishlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.profilefragment.WishListAdapter;
import com.shopping.bloom.databinding.ActivityWishListBinding;
import com.shopping.bloom.model.wishlist.WishList;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WishListActivity extends AppCompatActivity {

    private ActivityWishListBinding binding;
    private WishListAdapter adapter;
    private List<WishListData> list;
    private WishListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // setup toolbar
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitle("WishList");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // initialise view model
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);

        // initialise recyclerview,list,adapter
        list = new ArrayList<>();
        adapter = new WishListAdapter(list, this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getWishList("0", "12").observe(this, new Observer<WishList>() {
            @Override
            public void onChanged(WishList wishList) {
                list.clear();
                list.addAll(wishList.getWishListData());
                adapter.notifyDataSetChanged();
            }
        });


    }
}