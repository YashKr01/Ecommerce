package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.restService.response.NotificationResponse;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    List<NotificationResponse> responseList;

    public NotificationAdapter(Context context, List<NotificationResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    public void setResponseList(List<NotificationResponse> responseList){
        this.responseList = responseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(responseList.get(position).getTitle());
        holder.body.setText(responseList.get(position).getBody());
        holder.date.setText(responseList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if (this.responseList != null) {
            return this.responseList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       TextView title, body, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            date = itemView.findViewById(R.id.date);
        }
    }
}
