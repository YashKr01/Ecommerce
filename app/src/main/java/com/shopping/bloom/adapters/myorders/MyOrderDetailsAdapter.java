package com.shopping.bloom.adapters.myorders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.myorders.Order;
import com.shopping.bloom.utils.CommonUtils;

import java.util.List;

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.ProductViewHolder> {

    private List<Order> orderList;
    private Context context;

    public MyOrderDetailsAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_orders_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Order currentItem = orderList.get(position);
        holder.orderName.setText(currentItem.getOrderName());
        holder.orderPrice.setText(currentItem.getOrderPrice());
        holder.orderPaymentMethod.setText(currentItem.getPaymentMethod());
        holder.orderDate.setText(currentItem.getDeliveryDate());
        holder.orderNumber.setText("Order #" + currentItem.getOrderId());

        CommonUtils.loadImageWithGlide(
                context, currentItem.getOrderImage(), holder.imageView, true
        );

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView orderName, orderDate, orderPaymentMethod, orderPrice, orderNumber;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_product_order);
            orderName = itemView.findViewById(R.id.txt_order_name);
            orderDate = itemView.findViewById(R.id.txt_order_date);
            orderPaymentMethod = itemView.findViewById(R.id.txt_order_payment_method);
            orderPrice = itemView.findViewById(R.id.txt_order_price);
            orderNumber = itemView.findViewById(R.id.txt_order_number);

        }
    }


}
