package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ProductAdapter;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryFragment extends Fragment {

    private static final String TAG = ViewCategoryFragment.class.getName();

    private static final String ARG_parentId = "param1";
    private static final String ARG_categoryTitle = "param2";

    private String parentId;
    private String categoryTitle;

    //views
    private TextView tvSort, tvCategory;
    private RelativeLayout rlFilter;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;

    private ProductsViewModel viewModel;

    public ViewCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * ViewCategory fragment using the provided parameters.
     *
     * @param parentId Parameter 1.
     * @param categoryTitle Parameter 2.
     * @return A new instance of fragment ViewCategoryFragment.
     */
    public static ViewCategoryFragment newInstance(String parentId, String categoryTitle) {
        ViewCategoryFragment fragment = new ViewCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_parentId, parentId);
        args.putString(ARG_categoryTitle, categoryTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_parentId);
            categoryTitle = getArguments().getString(ARG_categoryTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_view_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();

        initViews(view);
        setUpRecycleView();
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        checkNetworkAndFetchData();
    }

    private void initViews(View view) {
        tvSort = view.findViewById(R.id.tvSort);
        tvCategory = view.findViewById(R.id.tvCategory);
        rlFilter = view.findViewById(R.id.rlFilter);
        rvProducts = view.findViewById(R.id.rvViewCategory);

        //Attack Click Listeners
        tvSort.setOnClickListener(optionsClickListener);
        tvCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);
    }

    private void setUpRecycleView() {
        adapter = new ProductAdapter(getContext());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(adapter);
        //adapter.updateList(getDummyData());
    }

    private void checkNetworkAndFetchData() {
        if(NetworkCheck.isConnect(getContext())){
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("2", 10);       // Temporary category
        } else {
            showNoInternetImage(true);
        }
    }

    private final ProductResponseListener responseListener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
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
            if(v.getId() == R.id.tvSort){
                Log.d(TAG, "onDebouncedClick: sort");
            }
            if(v.getId() == R.id.tvCategory){
                Log.d(TAG, "onDebouncedClick: Category");
            }
            if(v.getId() == R.id.rlFilter){
                Log.d(TAG, "onDebouncedClick: filter");
            }
        }
    };

    private void showNoInternetImage(boolean show) {
        if(show) {
            if(getContext() != null) {
                Toast.makeText(getContext(), "No internet", Toast.LENGTH_SHORT).show();
            }
        } else {
                Toast.makeText(getContext(), "internet available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view_category, menu);
    }

    private List<Product> getDummyData() {
        List<Product> list = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            Product product = new Product(1, "Dummy Name","","","","","WHITE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE",
                    "http://www.bloomapp.in/images/product/product1618639820.png","","",
                    "","","","","",
                    "");
            list.add(product);
        }
        return list;
    }

}













