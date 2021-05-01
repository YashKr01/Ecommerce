package com.shopping.bloom.bottomSheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.shopping.bloom.R;
import com.shopping.bloom.model.CategoryTypes;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.Const.FILTER;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.List;


public class FilterBottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    private static final String TAG = FilterBottomSheetDialog.class.getName();

    private BottomSheetBehavior mBehavior;
    List<String> colorFilterList, sizeFilterList, typeFilterList;
    private Context context;
    private LinearLayout lytCategory, lytColor, lytSize, lytType;
    Button btClear, btApply;
    private List<CategoryTypes> filterList;

    public FilterBottomSheetDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.bottom_sheet_filter_item, null);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // set data to view
        //((View) view.findViewById(R.id.lyt_spacer)).setMinimumHeight(getScreenHeight() / 2);
        mBehavior.setDraggable(false);

        initViews(view);
        btClear.setOnClickListener(this);
        btClear.setOnClickListener(this);
        Log.d(TAG, "onCreateDialog: ");

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        Log.d(TAG, "onActivityCreated: " + colorFilterList.toString());
        Log.d(TAG, "onActivityCreated: " + sizeFilterList.toString());
        Log.d(TAG, "onActivityCreated: " + typeFilterList.toString());

        addFilterToNavigationSheet(lytColor, colorFilterList, FILTER.COLOR);
        addFilterToNavigationSheet(lytSize, sizeFilterList, FILTER.LENGTH);
        addFilterToNavigationSheet(lytType, typeFilterList, FILTER.TYPE);
    }

    private void initViews(View view) {
        lytCategory = view.findViewById(R.id.llCategoryParentLayout);
        lytColor = view.findViewById(R.id.llColorParentLayout);
        lytSize = view.findViewById(R.id.llSizeParentLayout);
        lytType = view.findViewById(R.id.llTypeParentLayout);
        btApply = view.findViewById(R.id.btApply);
        btClear = view.findViewById(R.id.btClear);
    }

    public void updateFilterLists(
            List<String> colorFilterList,
            List<String> sizeFilterList,
            List<String> typeFilterList
    ) {
        this.colorFilterList = new ArrayList<>();
        this.sizeFilterList = new ArrayList<>();
        this.typeFilterList = new ArrayList<>();
        Log.d(TAG, "updateFilterLists: " + colorFilterList);
        if (colorFilterList != null && colorFilterList.size() > 0) {
            this.colorFilterList = colorFilterList;
            //addFilterToNavigationSheet(lytColor, colorFilterList, FILTER.COLOR);
        }
        if (sizeFilterList != null && sizeFilterList.size() > 0) {
            this.sizeFilterList = sizeFilterList;
            //addFilterToNavigationSheet(lytSize, sizeFilterList, FILTER.LENGTH);
        }
        if (typeFilterList != null && typeFilterList.size() > 0) {
            this.typeFilterList = typeFilterList;
            //addFilterToNavigationSheet(lytType, typeFilterList, FILTER.TYPE);
        }
    }

    public void updateCategoryList(List<CategoryTypes> filterList) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void addFilterToNavigationSheet(LinearLayout parentView, List<String> values, Const.FILTER tag) {
        Log.d(TAG, "addFilterToNavigationSheet: TAG: " + tag);
        if (values == null || values.isEmpty()) {
            Log.d(TAG, "addFilterToNavigationSheet: NULL Values for TAG: " + tag);
            parentView.removeAllViews();
            return;
        }
        parentView.removeAllViews();

        //Param for the textView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //params for the LinearLayout which will wrap the text view
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        LinearLayout currentRow = new LinearLayout(context);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        currentRow.setWeightSum(5.0f);
        currentRow.setPadding(10, 10, 10, 10);
        int rowItemCount = 0;
        for (String value : values) {
            TextView textView = new TextView(context);
            textView.setText(value);
            textView.setTextColor(ContextCompat.getColor(context, R.color.blue_grey_900));
            textView.setTextSize(14f);
            textView.setLayoutParams(params);
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_choices));
            textView.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    addToFilterToList(textView, tag);
                }
            });
            if (rowItemCount % 5 == 0 && rowItemCount != 0) {
                parentView.addView(currentRow);
                currentRow = new LinearLayout(context);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setWeightSum(5.0f);
                currentRow.setPadding(10, 10, 10, 10);
            }
            LinearLayout tvParentLayout = new LinearLayout(context);
            tvParentLayout.setOrientation(LinearLayout.VERTICAL);
            tvParentLayout.setLayoutParams(tvParams);
            tvParentLayout.addView(textView);
            currentRow.addView(tvParentLayout);
            rowItemCount++;
            Log.d(TAG, "addFilterToNavigationSheet: adding...");
        }
        if (currentRow.getChildCount() > 0)
            parentView.addView(currentRow);
    }

    /*
     *   @param tag will is used to identify if Filter type
     *       COLOR, TYPE, SIZE
     * */
    private void addToFilterToList(TextView textView, FILTER tag) {
        Log.d(TAG, "filterItem: TAG: " + tag.toString());
        Drawable background = textView.getBackground();
        if (background == null) return;
        String value = textView.getText().toString();
        if (background.getConstantState() ==
                ContextCompat.getDrawable(context, R.drawable.bg_color_choices).getConstantState()) {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_choices_selected));

            //Add item to the filter list based on tag
            if (tag == FILTER.COLOR) {
                colorFilterList.add(value);
            } else if (tag == FILTER.TYPE) {
                typeFilterList.add(value);
            } else if (tag == FILTER.LENGTH) {
                sizeFilterList.add(value);
            } else {
                Log.d(TAG, "filterItem: UNIDENTIFIED VALUE");
            }
        } else {
            textView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_choices));

            //Remove item to the filter list based on tag
            if (tag == FILTER.COLOR) {
                colorFilterList.remove(value);
            } else if (tag == FILTER.TYPE) {
                typeFilterList.remove(value);
            } else if (tag == FILTER.LENGTH) {
                sizeFilterList.remove(value);
            } else {
                Log.d(TAG, "filterItem: UNIDENTIFIED VALUE");
            }
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.btApply){

        }
        if(viewId == R.id.btClear){

        }
    }
}
