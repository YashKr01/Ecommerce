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
import com.shopping.bloom.model.fragmentnew.NewTrends;

import java.util.ArrayList;
import java.util.List;


public class NewTrendFragment extends Fragment {
    private static final String TAG = NewTrendFragment.class.getName();

    private FragmentNewTrendBinding binding;
    private NewTrendAdapter adapter;
    private List<NewTrends> list;

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

        initRecyclerView();
        initList(list);

        binding.recyclerView.setAdapter(adapter);

    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        adapter = new NewTrendAdapter(list, getContext());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initList(List<NewTrends> newTrends) {
        newTrends.add(new NewTrends(null,"New In Sweatshirts",
                "Hooded, Graphic, tie dye",R.color.blue_400));
        newTrends.add(new NewTrends(null,"New In Sweatshirts",
                "Hooded, Graphic, tie dye",R.color.blue_400));
        newTrends.add(new NewTrends(null,"New In Sweatshirts",
                "Hooded, Graphic, tie dye",R.color.blue_400));
        newTrends.add(new NewTrends(null,"New In Sweatshirts",
                "Hooded, Graphic, tie dye",R.color.blue_400));
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