package com.shopping.bloom.activities.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.adapters.search.SearchAdapter;
import com.shopping.bloom.databinding.ActivitySearchBinding;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.search.SearchActivityConfig;
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.search.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter adapter;
    private List<SearchProduct> list;
    private GridLayoutManager layoutManager;
    private String LIMIT = "20";
    private String PAGE = "0";
    private boolean IS_LOADING = false;
    private boolean IS_LAST_PAGE = false;

    private SearchActivityConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialise view model
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Remote Config data
        config = RemoteConfig.getTopSearchConfig(this);

        // swipe layout
        binding.swipeRefresh.setOnRefreshListener(this);

        // initialise list, adapter and recyclerview
        list = new ArrayList<>();
        adapter = new SearchAdapter(this, list);
        layoutManager = new GridLayoutManager(this, 2);
        binding.searchRecyclerView.setHasFixedSize(true);
        binding.searchRecyclerView.setLayoutManager(layoutManager);
        binding.searchRecyclerView.setAdapter(adapter);

        binding.edittextSearch.requestFocus();

        // search text view click listener
        binding.txtSearch.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if (!validInput()) {
                    Toast.makeText(SearchActivity.this, "Empty Search", Toast.LENGTH_SHORT).show();
                } else {
                    getSearchedProducts(binding.edittextSearch.getText().toString().trim());
                    closeKeyboard();
                }
            }
        });

        // Search icon on keyboard click listener
        binding.edittextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!validInput()) {
                    Toast.makeText(SearchActivity.this, "Empty Search", Toast.LENGTH_SHORT).show();
                } else {
                    getSearchedProducts(binding.edittextSearch.getText().toString().trim());
                    closeKeyboard();
                }

            }
            return true;
        });

        // back image click listener
        binding.imgBack.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                finish();
            }
        });

        // For pagination
        binding.searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // get input query
                String query = binding.edittextSearch.getText().toString().trim();
                if (!binding.searchRecyclerView.canScrollVertically(1)) {
                    if (!IS_LAST_PAGE && !IS_LOADING) {
                        // Increase page number
                        PAGE = String.valueOf(Integer.parseInt(PAGE) + 1);
                        // get next page products
                        getSearchedProducts(query);
                        Log.d("ffffffffff", "onScrollStateChanged: called");
                    }
                }
            }

        });

    }

    // get searched query product list
    private void getSearchedProducts(String query) {

        IS_LOADING = true;
        binding.progressBar3.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            viewModel.getSearchProducts(LIMIT, PAGE, query).observe(this, searchProducts -> {
                if (searchProducts != null && searchProducts.size() > 0) {
                    int oldListSize = list.size();
                    list.addAll(searchProducts);
                    adapter.notifyItemRangeInserted(oldListSize, list.size());
                    binding.txtEmpty.setVisibility(View.GONE);
                } else {

                    IS_LAST_PAGE = true;
                    if (list.isEmpty() && searchProducts == null) {
                        IS_LAST_PAGE = false;
                        binding.txtEmpty.setVisibility(View.VISIBLE);
                    }

                }
                IS_LOADING = false;
                binding.progressBar3.setVisibility(View.INVISIBLE);
            });
            showNoConnection(false);
        } else {
            // no connection handling
            showNoConnection(true);
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();

        if (view != null) {

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean validInput() {
        String input = binding.edittextSearch.getText().toString().trim();
        if (input.isEmpty()) {
            Toast.makeText(this, "Empty Search!!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void showNoConnection(boolean show) {
        if (show) {
            binding.newFragmentNoConnectionLayout.setVisibility(View.VISIBLE);
            binding.progressBar3.setVisibility(View.INVISIBLE);
        } else binding.newFragmentNoConnectionLayout.setVisibility(View.GONE);
    }


    @Override
    public void onRefresh() {
        if (!NetworkCheck.isConnect(this)) {
            showNoConnection(true);
        } else {
            showNoConnection(false);
            list.clear();
            PAGE = "0";

            if (validInput())
                getSearchedProducts(binding.edittextSearch.getText().toString().trim());

        }
        binding.swipeRefresh.setRefreshing(false);
    }

}