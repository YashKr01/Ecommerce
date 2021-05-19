package com.shopping.bloom.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shopping.bloom.adapters.MyOrdersAdapter;
import com.shopping.bloom.databinding.ActivityMyOrdersBinding;
import com.shopping.bloom.model.myorders.MyOrders;
import com.shopping.bloom.model.myorders.Order;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private ActivityMyOrdersBinding binding;

    // URL for loading temporary image
    public static final String IMAGE_URL = "http://bloomapp.in/images/product/product_image_3.png";

    private MyOrdersAdapter adapter;
    private List<MyOrders> myOrdersList;
    private Integer RECYCLER_VIEW_POSITION;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get passed position
        RECYCLER_VIEW_POSITION = getIntent().getIntExtra("RECYCLER_VIEW_POSITION", 0);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup recycler view and adapter
        myOrdersList = new ArrayList<>();
        adapter = new MyOrdersAdapter(myOrdersList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // setup mock data for recycler view
        myOrdersList.add(new MyOrders("Unpaid Orders", getList()));
        myOrdersList.add(new MyOrders("Processed Orders", getList()));
        myOrdersList.add(new MyOrders("Shipped Orders", getList()));
        myOrdersList.add(new MyOrders("Cancelled Orders", getList()));

        binding.recyclerView.getLayoutManager().scrollToPosition(RECYCLER_VIEW_POSITION);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
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

}
