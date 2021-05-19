package com.shopping.bloom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.SearchAdapter;
import com.shopping.bloom.databinding.ActivitySearchBinding;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.search.SearchActivityConfig;
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.restService.callback.SearchProductClickListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchProductClickListener {

    private ActivitySearchBinding binding;
    private SearchViewModel viewModel;
    private SearchAdapter adapter;
    private List<SearchProduct> list;
    private GridLayoutManager layoutManager;
    private String LIMIT = "20";
    private String PAGE = "0";
    private boolean IS_LOADING = false;
    private boolean IS_LAST_PAGE = false;
    private LinearLayout parentLayout;
    private View noConnectionLayout;

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
        List<String> topSearchesList = config.getTop_searches();

        // setting custom layout of top searches
        parentLayout = (LinearLayout) findViewById(R.id.top_search_layout);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;
        for (int i = 0; i < topSearchesList.size(); i++) {
            view = layoutInflater.inflate(R.layout.item_top_searches, parentLayout, false);
            TextView textView = (TextView) view.findViewById(R.id.txt_top_search);
            textView.setText(topSearchesList.get(i));
            parentLayout.addView(textView);
            textView.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    binding.edittextSearch.setText(textView.getText());
                }
            });
        }




        // initialise list, adapter and recyclerview
        list = new ArrayList<>();
        adapter = new SearchAdapter(this, list, this);
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
                    if (!IS_LAST_PAGE && !IS_LOADING && !query.isEmpty()) {
                        // Increase page number
                        PAGE = String.valueOf(Integer.parseInt(PAGE) + 1);
                        // get next page products
                        getSearchedProducts(query);
                    }
                }
            }

        });


        noConnectionLayout = binding.newFragmentNoConnectionLayout.inflate();
        TextView textView = noConnectionLayout.findViewById(R.id.tvSwipeToRefresh);
        textView.setText(R.string.click_to_refresh);
        textView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if (!binding.edittextSearch.getText().toString().trim().isEmpty()) {
                    getSearchedProducts(binding.edittextSearch.getText().toString().trim());
                    closeKeyboard();
                }
                checkConnection(SearchActivity.this);
            }
        });

        checkConnection(this);

        binding.edittextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                list.clear();
                adapter.notifyDataSetChanged();
                if (s.toString().isEmpty()) {
                    binding.topLayout.setVisibility(View.VISIBLE);
                    binding.txtEmpty.setVisibility(View.GONE);
                }
                PAGE = "0";
                IS_LAST_PAGE = false;
            }
        });

    }

    private void checkConnection(Context context) {
        if (NetworkCheck.isConnect(context)) {
            showNoConnection(false);
        } else showNoConnection(true);
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
                    binding.topLayout.setVisibility(View.GONE);

                } else {
                    IS_LAST_PAGE = true;

                    if (list.isEmpty() && searchProducts == null) {
                        binding.topLayout.setVisibility(View.GONE);
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
            return false;
        } else
            return true;

    }

    private void showNoConnection(boolean show) {
        if (show) {
            binding.newFragmentNoConnectionLayout.setVisibility(View.VISIBLE);
            binding.progressBar3.setVisibility(View.INVISIBLE);
        } else binding.newFragmentNoConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSearchClickListener(SearchProduct searchProduct) {
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", searchProduct.getId());
        startActivity(intent);
    }
}