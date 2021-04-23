package com.shopping.bloom.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ProductAdapter;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewCategoryActivity extends AppCompatActivity {

    private static final String TAG = ViewCategoryActivity.class.getName();

    private ProductsViewModel viewModel;
    //views
    private TextView tvSort, tvCategory;
    private RelativeLayout rlFilter;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        initViews();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setUpRecycleView();
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        checkNetworkAndFetchData();
    }

    private void initViews() {
        tvSort = findViewById(R.id.tvSort);
        tvCategory = findViewById(R.id.tvCategory);
        rlFilter = findViewById(R.id.rlFilter);
        rvProducts = findViewById(R.id.rvViewCategory);
        toolbar = findViewById(R.id.toolbar);

        //Attack Click Listeners
        tvSort.setOnClickListener(optionsClickListener);
        tvCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);
    }

    private void setUpRecycleView() {
        adapter = new ProductAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(adapter);
        //adapter.updateList(getDummyData());
    }

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(this)) {
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("2", 10);       // Temporary category
        } else {
            showNoInternetImage(true);
        }
    }

    private final ProductResponseListener responseListener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            Log.d(TAG, "onSuccess: View Category");
            showNoInternetImage(false);
            adapter.updateList(products);
            Log.d(TAG, "onSuccess: products " + products.toString());

        }

        @Override
        public void onFailure(int errorCode, String message) {
            Log.d(TAG, "onFailure: errorCode " + errorCode + " message " + message);
        }
    };

    private final DebouncedOnClickListener optionsClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.tvSort) {
                Log.d(TAG, "onDebouncedClick: sort");
            }
            if (v.getId() == R.id.tvCategory) {
                Log.d(TAG, "onDebouncedClick: Category");
            }
            if (v.getId() == R.id.rlFilter) {
                Log.d(TAG, "onDebouncedClick: filter");
            }
        }
    };

    private void showNoInternetImage(boolean show) {
        if (show) {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "internet available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private List<Product> getDummyData() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Product product = new Product(1, "Dummy Name", "", "100", "1000", "", "WHITE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE",
                    "/images/product/product1618639820.png", "", "",
                    "100", "", "", "", "",
                    "");
            list.add(product);
        }
        return list;
    }

}