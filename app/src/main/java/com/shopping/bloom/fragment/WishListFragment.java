package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.WishListAdapter;
import com.shopping.bloom.databinding.FragmentWishListBinding;
import com.shopping.bloom.models.DummyDataModel;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private FragmentWishListBinding binding;
    private List<DummyDataModel> list;
    private WishListAdapter adapter;

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater, container, false);

        // RecyclerView setup
        list = new ArrayList<>();
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        list.add(new DummyDataModel(R.drawable.ic_launcher_foreground, "100"));
        adapter = new WishListAdapter(list);
        binding.wishListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        binding.wishListRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }
}