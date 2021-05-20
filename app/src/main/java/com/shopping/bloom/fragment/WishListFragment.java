package com.shopping.bloom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.shopping.bloom.activities.RecentlyViewedActivity;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.activities.WishListActivity;
import com.shopping.bloom.adapters.WishListAdapter;
import com.shopping.bloom.databinding.FragmentWishListBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.WishListViewModel;

import java.util.List;

import static com.shopping.bloom.utils.Const.REQ_SINGLE_PRODUCT;

public class WishListFragment extends Fragment implements ProductClickListener {
    private static final String TAG = WishListFragment.class.getName();

    private FragmentWishListBinding binding;
    private WishListAdapter adapter;
    private WishListViewModel viewModel;

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWishListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);
        setUpRecyclerView();

        binding.tvViewMore.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                gotoWishListActivity();
            }
        });

        checkNetworkAndFetchData();
    }

    private void setUpRecyclerView() {
        adapter = new WishListAdapter(getContext(), this);
        binding.rvRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvRecyclerView.setAdapter(adapter);
        binding.rvRecyclerView.setNestedScrollingEnabled(false);
    }

    private void checkNetworkAndFetchData() {
        if (!NetworkCheck.isConnect(getContext())) {
            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            binding.progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.getWishList(0, 6, listener);
    }

    private final ProductResponseListener listener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            if (products != null && products.size() > 0) {
                adapter.updateList(products);
                binding.tvEmpty.setVisibility(View.INVISIBLE);
                binding.tvViewMore.setVisibility(View.VISIBLE);
                binding.rvRecyclerView.setVisibility(View.VISIBLE);
            } else {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.tvViewMore.setVisibility(View.INVISIBLE);
                binding.rvRecyclerView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.tvViewMore.setVisibility(View.INVISIBLE);
            binding.rvRecyclerView.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void gotoWishListActivity() {
        Intent intent = new Intent(getActivity(), WishListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProductClicked(Product product) {
        String CALLING_ACTIVITY = WishListFragment.class.getName();
        String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
        Intent intent = new Intent(getActivity(), SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra(ARG_CALLING_ACTIVITY, CALLING_ACTIVITY);
        startActivityForResult(intent, REQ_SINGLE_PRODUCT);
    }
}