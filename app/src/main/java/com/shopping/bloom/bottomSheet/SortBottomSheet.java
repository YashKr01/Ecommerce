package com.shopping.bloom.bottomSheet;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopping.bloom.R;
import com.shopping.bloom.restService.callback.SortByListener;
import com.shopping.bloom.utils.Const;

public class SortBottomSheet extends BottomSheetDialog {
    private static final String TAG = SortBottomSheet.class.getName();

    SortByListener mListener = null;
    Const.SORT_BY defaultValue;
    Context context;

    LinearLayout lytMostPopular, lytNewArrival, lytPriceHtoL, lytPriceLtoH;
    RadioButton rdbMostPopular, rdbNewArrival, rdbPriceHtoL, rdbPriceLtoH;

    public SortBottomSheet(@NonNull Context context) {
        super(context);
        this.context = context;
        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_sort_item, null);
        setContentView(view);

        initViews(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setDefaultRadioButton(Const.SORT_BY defaultValue) {
        if(defaultValue == Const.SORT_BY.MOST_POPULAR) rdbMostPopular.setChecked(true);
        if(defaultValue == Const.SORT_BY.NEW_ARRIVAL) rdbNewArrival.setChecked(true);
        if(defaultValue == Const.SORT_BY.PRICE_HIGH_TO_LOW) rdbPriceHtoL.setChecked(true);
        if(defaultValue == Const.SORT_BY.PRICE_LOW_TO_HIGH) rdbPriceLtoH.setChecked(true);
    }

    private void initViews(View view) {
        lytMostPopular = view.findViewById(R.id.lyt_MostPopular);
        lytNewArrival = view.findViewById(R.id.lyt_NewArrival);
        lytPriceHtoL = view.findViewById(R.id.lyt_PriceHtoL);
        lytPriceLtoH = view.findViewById(R.id.lyt_PriceLtoH);

        rdbMostPopular = view.findViewById(R.id.rdbMostPopular);
        rdbNewArrival = view.findViewById(R.id.rdbNewArrival);
        rdbPriceHtoL = view.findViewById(R.id.rdbPriceHtoL);
        rdbPriceLtoH = view.findViewById(R.id.rdbPriceLtoH);


        //attach click listener
        lytMostPopular.setOnClickListener(clickListener);
        lytNewArrival.setOnClickListener(clickListener);
        lytPriceHtoL.setOnClickListener(clickListener);
        lytPriceLtoH.setOnClickListener(clickListener);

        rdbMostPopular.setOnClickListener(clickListener);
        rdbNewArrival.setOnClickListener(clickListener);
        rdbPriceHtoL.setOnClickListener(clickListener);
        rdbPriceLtoH.setOnClickListener(clickListener);

    }

    public void setListenerAndDefaultValue(SortByListener listener, Const.SORT_BY defaultValue) {
        this.mListener = listener;
        this.defaultValue = defaultValue;
        if(defaultValue != null) setDefaultRadioButton(defaultValue);
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.lyt_MostPopular || viewId == R.id.rdbMostPopular) {
                setItem(rdbMostPopular);
                if (mListener != null)
                    mListener.onSortSelected(Const.SORT_BY.MOST_POPULAR, "Most Popular");
                return;
            }
            if (viewId == R.id.lyt_NewArrival || viewId == R.id.rdbNewArrival) {
                setItem(rdbNewArrival);
                if (mListener != null)
                    mListener.onSortSelected(Const.SORT_BY.NEW_ARRIVAL, "New Arrival");
                return;
            }
            if (viewId == R.id.lyt_PriceHtoL || viewId == R.id.rdbPriceHtoL) {
                setItem(rdbPriceHtoL);
                if (mListener != null)
                    mListener.onSortSelected(Const.SORT_BY.PRICE_HIGH_TO_LOW, "Price High To Low");
                return;
            }
            if (viewId == R.id.lyt_PriceLtoH || viewId == R.id.rdbPriceLtoH) {
                setItem(rdbPriceLtoH);
                if (mListener != null)
                    mListener.onSortSelected(Const.SORT_BY.PRICE_LOW_TO_HIGH, "Price Low To High");
                return;
            }
        }
    };

    private void setItem(RadioButton radioButton) {
        int[] ids = {R.id.rdbMostPopular, R.id.rdbNewArrival, R.id.rdbPriceHtoL, R.id.rdbPriceLtoH};
        for (int id : ids) {
            RadioButton radioButton1 = this.findViewById(id);
            boolean matched = radioButton.getId() == id;
            if (matched) {
                radioButton.setChecked(true);
            } else {
                radioButton1.setChecked(false);
            }
        }
    }
}
