package com.shopping.bloom.utils;

import android.content.Context;
import android.widget.ImageView;

public class RecommendedImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = RecommendedImageView.class.getName();

    public RecommendedImageView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
