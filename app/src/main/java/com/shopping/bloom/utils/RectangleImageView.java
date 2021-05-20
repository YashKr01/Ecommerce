package com.shopping.bloom.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RectangleImageView extends androidx.appcompat.widget.AppCompatImageView {
    public RectangleImageView(Context context) {
        super(context);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth() , (int) (getMeasuredWidth()  * 1.3f)); //Snap to width
    }
}