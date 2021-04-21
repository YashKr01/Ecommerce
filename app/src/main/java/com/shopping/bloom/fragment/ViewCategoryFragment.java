package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;

public class ViewCategoryFragment extends Fragment {

    private static final String TAG = ViewCategoryFragment.class.getName();

    private static final String ARG_parentId = "param1";
    private static final String ARG_categoryTitle = "param2";

    private String parentId;
    private String categoryTitle;

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
}