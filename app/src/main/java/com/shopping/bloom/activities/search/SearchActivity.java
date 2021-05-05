package com.shopping.bloom.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
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
    private String LIMIT = "20", PAGE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        list = new ArrayList<>();
        adapter = new SearchAdapter(this, list);
        binding.searchRecyclerView.setHasFixedSize(true);
        binding.searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.searchRecyclerView.setAdapter(adapter);

        binding.edittextSearch.requestFocus();


        binding.txtSearch.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if (binding.edittextSearch.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Empty Search", Toast.LENGTH_SHORT).show();
                } else {
                    binding.edittextSearch.clearFocus();
                    getSearchedProducts(binding.edittextSearch.getText().toString().trim());
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

    private void getSearchedProducts(String query) {

        binding.progressBar3.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            viewModel.getSearchProducts(LIMIT, PAGE, query).observe(this, searchProducts -> {
                if (searchProducts != null && searchProducts.size() > 0) {
                    list.clear();
                    list.addAll(searchProducts);
                    adapter.notifyDataSetChanged();
                    binding.progressBar3.setVisibility(View.INVISIBLE);
                } else {
                    list.clear();
                    binding.progressBar3.setVisibility(View.INVISIBLE);
                    Snackbar.make(binding.getRoot(), "No matching results found", Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        }


    }

}