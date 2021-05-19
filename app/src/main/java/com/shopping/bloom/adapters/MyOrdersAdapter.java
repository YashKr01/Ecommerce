package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.myorders.MyOrders;
import com.shopping.bloom.model.myorders.Order;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.OrdersViewHolder> {

    private List<MyOrders> myOrdersList;
    private Context context;

    public MyOrdersAdapter(List<MyOrders> myOrdersList, Context context) {
        this.myOrdersList = myOrdersList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_my_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        MyOrders currentItem = myOrdersList.get(position);
        holder.textView.setText(currentItem.getStatus());

        setOrderDetailsRecyclerView(holder.recyclerView, currentItem.getOrderList());

    }

    @Override
    public int getItemCount() {
        return myOrdersList.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RecyclerView recyclerView;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txt_order_status);
            recyclerView = itemView.findViewById(R.id.child_order_recycler_view);
        }
    }

    private void setOrderDetailsRecyclerView(RecyclerView recyclerView, List<Order> list) {
        MyOrderDetailsAdapter adapter = new MyOrderDetailsAdapter(list, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

}
