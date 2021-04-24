package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.FaqAdapter;
import com.shopping.bloom.model.FaqModel;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView headerTextView;
    List<FaqModel> faqList;
    FaqAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        recyclerView = findViewById(R.id.recyclerView);
        headerTextView = findViewById(R.id.headerTextView);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener( v-> onBackPressed());

        headerTextView.setText(getIntent().getStringExtra("Title"));

        faqList = new ArrayList<>();

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));
        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));
        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));
        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));
        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));
        faqList.add(new FaqModel("How To?", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."));

        adapter = new FaqAdapter(this, faqList);
        recyclerView.setAdapter(adapter);


    }
}