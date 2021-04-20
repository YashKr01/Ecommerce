package com.shopping.bloom.fragment;

import android.content.Context;
import android.net.Network;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.newfragment.NewTrendAdapter;
import com.shopping.bloom.databinding.FragmentNewTrendBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.fragmentnew.NewTrends;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;


public class NewTrendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NewTrendFragment.class.getName();

    private FragmentNewTrendBinding binding;
    private NewTrendAdapter adapter;
    private List<NewTrends> list;

    // URL for loading temporary image
    public static final String IMAGE_URL = "http://bloomapp.in/images/product/product_image_3.png";

    public NewTrendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewTrendBinding.inflate(inflater, container, false);

        // swipe refresh listener
        binding.newTrendsSwipeRefresh.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();

        list = new ArrayList<>();
        initList(list);
        initRecyclerView();

        binding.recyclerView.setAdapter(adapter);
    }

    // check device connection
    private boolean isConnectionEnabled(Context context) {
        if (NetworkCheck.isConnect(context)) {
            Log.d(TAG, "isConnectionEnabled: " + " TRUE");
            return true;
        } else {
            Log.d(TAG, "isConnectionEnabled: " + " FALSE");
            Toast.makeText(context, R.string.no_internet_connected, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // initialise recycler view
    private void initRecyclerView() {
        adapter = new NewTrendAdapter(list, getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // function for getting mock data
    private List<Product> getMockData() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Product(1, null, null, null,
                    null, IMAGE_URL, "1999", null, null
                    , null));
        }
        return list;
    }

    // initialise list
    private void initList(List<NewTrends> newTrends) {

        newTrends.add(new NewTrends(null, "New In Kurtis", "Printed, Graphic"
                , R.color.yellow_200, getMockData()));
        newTrends.add(new NewTrends(null, "New In SweatShirts", "Hooded, Cotton"
                , R.color.orange_100, getMockData()));
        newTrends.add(new NewTrends(null, "New In Jeans", "Washable, Graphic"
                , R.color.red_100, getMockData()));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(true);
        MenuItem item = menu.findItem(R.id.menu_settings);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: new" + menu.getItem(0).getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {

        if (isConnectionEnabled(getContext())) {
            list.clear();
            initList(list);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }

        binding.newTrendsSwipeRefresh.setRefreshing(false);
    }

}