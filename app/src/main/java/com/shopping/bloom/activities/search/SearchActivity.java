package com.shopping.bloom.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.search.SearchAdapter;
import com.shopping.bloom.databinding.ActivitySearchBinding;
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter adapter;
    private List<SearchProduct> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        list = new ArrayList<>();
        adapter = new SearchAdapter(this, list);
        binding.searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.searchRecyclerView.setAdapter(adapter);


        binding.txtSearch.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if (binding.edittextSearch.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Empty Search", Toast.LENGTH_SHORT).show();
                } else {
                    getSearchedProducts();
                }
            }
        });

        binding.imgBack.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                finish();
            }
        });


    }

    private void getSearchedProducts() {

        binding.progressBar3.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            viewModel.getSearchProducts().observe(this, new Observer<List<SearchProduct>>() {
                @Override
                public void onChanged(List<SearchProduct> searchProducts) {
                    if (searchProducts != null && searchProducts.size() > 0) {
                        list.size();
                        list.addAll(searchProducts);
                        adapter.notifyDataSetChanged();
                        binding.progressBar3.setVisibility(View.INVISIBLE);
                    } else {
                        binding.progressBar3.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        }


    }

}