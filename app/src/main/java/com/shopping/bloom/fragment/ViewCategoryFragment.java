package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopping.bloom.R;
import com.shopping.bloom.utils.DebouncedOnClickListener;

public class ViewCategoryFragment extends Fragment {

    private static final String TAG = ViewCategoryFragment.class.getName();

    private static final String ARG_parentId = "param1";
    private static final String ARG_categoryTitle = "param2";

    private String parentId;
    private String categoryTitle;

    //views
    private TextView tvSort, tvCategory;
    private RelativeLayout rlFilter;
    private RecyclerView rvProducts;

    public ViewCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * ViewCategory fragment using the provided parameters.
     *
     * @param parentId Parameter 1.
     * @param categoryTitle Parameter 2.
     * @return A new instance of fragment ViewCategoryFragment.
     */
    public static ViewCategoryFragment newInstance(String parentId, String categoryTitle) {
        ViewCategoryFragment fragment = new ViewCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_parentId, parentId);
        args.putString(ARG_categoryTitle, categoryTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentId = getArguments().getString(ARG_parentId);
            categoryTitle = getArguments().getString(ARG_categoryTitle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    private void initViews(View view) {
        tvSort = view.findViewById(R.id.tvSort);
        tvCategory = view.findViewById(R.id.tvCategory);
        rlFilter = view.findViewById(R.id.rlFilter);
        rvProducts = view.findViewById(R.id.rvViewCategory);

        //Attack Click Listeners
        tvSort.setOnClickListener(optionsClickListener);
        tvCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);



    }

    private final DebouncedOnClickListener optionsClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            if(v.getId() == R.id.tvSort){
                Log.d(TAG, "onDebouncedClick: sort");
            }
            if(v.getId() == R.id.tvCategory){
                Log.d(TAG, "onDebouncedClick: Category");
            }
            if(v.getId() == R.id.rlFilter){
                Log.d(TAG, "onDebouncedClick: filter");
            }
        }
    };



}













