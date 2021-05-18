package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.NotificationAdapter;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.restService.response.NotificationResponse;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    NotificationAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<NotificationResponse> notificationResponses;
    ViewStub viewStub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        viewStub = findViewById(R.id.vsEmptyScreen);

        notificationResponses = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        getNotification();

        checkNetworkConnectivity();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            checkNetworkConnectivity();
            getNotification();
            adapter.setResponseList(notificationResponses);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

    }

    private void getNotification() {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            notificationResponses = EcommerceDatabase.getInstance().notificationDao().getNotification();
            adapter = new NotificationAdapter(this, notificationResponses);
            recyclerView.setAdapter(adapter);
        });
    }

    private void checkNetworkConnectivity() {

        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);

    }

}