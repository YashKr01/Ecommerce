package com.shopping.bloom.fragment;

import android.content.Context;
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
import com.shopping.bloom.adapters.newfragment.NewAdapter;
import com.shopping.bloom.databinding.FragmentNewBinding;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.MainScreenConfig;
import com.shopping.bloom.model.MainScreenImageModel;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.fragmentnew.NewTrend;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;


public class NewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NewFragment.class.getName();

    private FragmentNewBinding binding;
    private NewAdapter adapter;
    private List<NewTrend> list;

    private MainScreenConfig mainScreenConfig;

    // URL for loading temporary image
    public static final String IMAGE_URL = "http://bloomapp.in/images/product/product_image_3.png";

    public NewFragment() {
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
        binding = FragmentNewBinding.inflate(inflater, container, false);

        // REMOTE CONFIG
        mainScreenConfig = RemoteConfig.getMainScreenConfig(getContext());

        // swipe refresh listener
        binding.newTrendsSwipeRefresh.setOnRefreshListener(this);
        return binding.getRoot();
    }

    // loading images from remote config
    private void loadRemoteConfigImages() {

        if (getContext() == null || mainScreenConfig == null) return;

        if (mainScreenConfig.getOfferImages().size() >= 2) {

            CommonUtils.loadImageWithGlide(getContext(),
                    mainScreenConfig.getOfferImages().get(0).getImagepath(),
                    binding.image1, true);

            CommonUtils.loadImageWithGlide(getContext(),
                    mainScreenConfig.getOfferImages().get(1).getImagepath(),
                    binding.image2, true);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();

        list = new ArrayList<>();
        initList(list);
        initRecyclerView();

        loadRemoteConfigImages();

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
        adapter = new NewAdapter(list, getContext());
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
    private void initList(List<NewTrend> newTrends) {

        newTrends.add(new NewTrend(null, "New In Kurtis", "Printed, Graphic"
                , R.color.yellow_200, getMockData()));
        newTrends.add(new NewTrend(null, "New In SweatShirts", "Hooded, Cotton"
                , R.color.orange_100, getMockData()));
        newTrends.add(new NewTrend(null, "New In Jeans", "Washable, Graphic"
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
            //Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }

        binding.newTrendsSwipeRefresh.setRefreshing(false);
    }

}