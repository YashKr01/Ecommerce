package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.newfragment.NewTrendAdapter;
import com.shopping.bloom.databinding.FragmentNewTrendBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.fragmentnew.NewTrends;

import java.util.ArrayList;
import java.util.List;


public class NewTrendFragment extends Fragment {
    private static final String TAG = NewTrendFragment.class.getName();

    private FragmentNewTrendBinding binding;
    private NewTrendAdapter adapter;
    private List<NewTrends> list;

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

    private void initRecyclerView() {
        adapter = new NewTrendAdapter(list, getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private List<Product> getMockData() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Product(1, null, null, null,
                    null, IMAGE_URL, "1999", null, null
                    , null));
        }
        return list;
    }

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

}