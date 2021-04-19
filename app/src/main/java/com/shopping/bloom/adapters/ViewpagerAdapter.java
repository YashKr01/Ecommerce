package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.MainScreenImageModel;
import com.shopping.bloom.restService.callback.ViewPagerClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends RecyclerView.Adapter<ViewpagerAdapter.ViewPagerViewHolder> {
    private static final String TAG = ViewpagerAdapter.class.getName();


    ArrayList<MainScreenImageModel> imageModels;
    ViewPagerClickListener mListener;
    Context mContext;

    public ViewpagerAdapter(List<MainScreenImageModel> list, Context context, ViewPagerClickListener listener) {
        imageModels = new ArrayList<>(list);
        mListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_viewpager_card,
                parent, false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        MainScreenImageModel imageModel = getItemAtPosition(position);

        CommonUtils.loadImageWithGlide(mContext, imageModel.getImagepath(), holder.imgCard, false);
        holder.imgCard.setOnClickListener((view -> mListener.onClick(imageModel)));
    }

    static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCard;
        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.imgCard);
        }
    }

    private MainScreenImageModel getItemAtPosition(int position) {
        if(position < 0 || position > imageModels.size()) {
            Log.d(TAG, "getItemAtPosition: INVALID POSITION");
        }
        return imageModels.get(position);
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }
}
