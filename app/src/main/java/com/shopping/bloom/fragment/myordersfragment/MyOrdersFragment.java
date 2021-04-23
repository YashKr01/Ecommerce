package com.shopping.bloom.fragment.myordersfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.myorders.MyOrdersAdapter;
import com.shopping.bloom.databinding.FragmentMyOrdersBinding;
import com.shopping.bloom.model.myorders.MyOrders;
import com.shopping.bloom.model.myorders.Order;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersFragment extends Fragment {

    private FragmentMyOrdersBinding binding;

    // URL for loading temporary image
    public static final String IMAGE_URL = "http://bloomapp.in/images/product/product_image_3.png";

    private MyOrdersAdapter adapter;
    List<MyOrders> myOrdersList;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myOrdersList = new ArrayList<>();
        adapter = new MyOrdersAdapter(myOrdersList, getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        myOrdersList.add(new MyOrders("Pending Orders", getList()));
        myOrdersList.add(new MyOrders("Past Orders", getList()));
        myOrdersList.add(new MyOrders("Cancelled Orders", getList()));

    }

    // method for generating mock list
    private List<Order> getList() {
        List<Order> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new Order("12763", "Pantaloon's Women High Low Dress",
                    "COD", "Exp. Delivery by Sun, June 21",
                    "$40", IMAGE_URL));
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}