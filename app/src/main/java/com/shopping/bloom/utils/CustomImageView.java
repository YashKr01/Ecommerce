package com.shopping.bloom.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import com.shopping.bloom.R;

/*
*   Custom ImageView
*   By default this image view will use the height equals to its width
*   The height could be change with the custom XML attribute [height_proportion]
*   to use this attribute inside xml file make sure to use custom namespace
*   Example:
*   custom:height_proportion="1.42" or app:height_proportion="1.3"
*   must include namespace at root layout [xmlns:custom="http://schemas.android.com/apk/res-auto"]
*   value type = float
*   Default value = "1.0"
*   value must be less than 2.0
* */
public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final String TAG = CustomImageView.class.getName();
    private float heightProportion = 1.0f;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomImageView,
                0, 0);

        try {
            heightProportion = a.getFloat(R.styleable.CustomImageView_height_proportion, 1.0f);
            if(heightProportion > 2.1) heightProportion = 1.0f;
            Log.d(TAG, "RecommendedImageView: " + heightProportion);
        } finally {
            a.recycle();
        }
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth() , (int) (getMeasuredWidth()  * heightProportion)); //Snap to width
    }
}
