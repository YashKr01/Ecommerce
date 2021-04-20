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
import com.shopping.bloom.model.fragmentnew.Child;
import com.shopping.bloom.model.fragmentnew.NewTrends;

import java.util.ArrayList;
import java.util.List;


public class NewTrendFragment extends Fragment {
    private static final String TAG = NewTrendFragment.class.getName();

    private FragmentNewTrendBinding binding;
    private NewTrendAdapter adapter;
    private List<NewTrends> list;

    private List<Child> list1;
    private List<Child> list2;
    private List<Child> list3;

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


        list1 = new ArrayList<>();
        list1.add(new Child(null, R.color.blue_400, 1999));
        list1.add(new Child(null, R.color.red_200, 499));
        list1.add(new Child(null, R.color.grey_200, 1999));
        list1.add(new Child(null, R.color.orange_100, 299));

        list2 = new ArrayList<>();
        list2.add(new Child(null, R.color.blue_400, 2000));
        list2.add(new Child(null, R.color.yellow_600, 400));
        list2.add(new Child(null, R.color.blue_400, 2009));
        list2.add(new Child(null, R.color.green_600, 200));

        list3 = new ArrayList<>();
        list3.add(new Child(null, R.color.black, 200));
        list3.add(new Child(null, R.color.blue_600, 4900));
        list3.add(new Child(null, R.color.yellow_300, 2070));
        list3.add(new Child(null, R.color.orange_500, 200));

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
        newTrends.add(new NewTrends(null, "New In Kurtis", "Printed, Graphic"
                , R.color.yellow_200, list1));
        newTrends.add(new NewTrends(null, "New In SweatShirts", "Hooded, Cotton"
                , R.color.orange_100, list2));
        newTrends.add(new NewTrends(null, "New In Jeans", "Washable, Graphic"
                , R.color.red_100, list3));
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