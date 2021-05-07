package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerImageAdapter extends PagerAdapter {

    List<String> imageList;
    Context context;


    public ViewPagerImageAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;

    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (this.imageList != null) {
            return this.imageList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (0.7f);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        List<Drawable> list = new ArrayList<>();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image_single_product, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setTag(position);
        Glide.with(container.getContext()).load(Const.GET_CATEGORY_DATA + imageList.get(position))
                .placeholder(R.drawable.placeholder_image)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
