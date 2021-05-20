package com.shopping.bloom.utils;

import android.content.Context;
import android.util.AttributeSet;

public class RecommendedImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = RecommendedImageView.class.getName();

    public RecommendedImageView(Context context) {
        super(context);
    }

    public RecommendedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecommendedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth() , (int) (getMeasuredWidth()  * 1.42f)); //Snap to width
    }
}
