package com.shopping.bloom.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.FaqAdapter;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.FaqModel;
import com.shopping.bloom.model.faq.FaqConfig;
import com.shopping.bloom.model.faq.faqModel;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    //    TextView headerTextView;
    List<FaqModel> faqList;
    FaqAdapter adapter;
    FaqConfig faqConfig;
    Toolbar toolbar;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);

        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String title = getIntent().getStringExtra("Title");

        faqConfig = RemoteConfig.getFaqConfig(this);
        faqList = new ArrayList<>();

        List<faqModel> orderList = faqConfig.getOrder_Issues();
        List<faqModel> deliveryList = faqConfig.getDelivery();
        List<faqModel> returnList = faqConfig.getReturn_Refund();
        List<faqModel> accountList = faqConfig.getAccount();
        List<faqModel> paymentList = faqConfig.getPayment_Promos();
        List<faqModel> productList = faqConfig.getProduct_Stock();

        for(int i = 0; i<orderList.size(); i++){
            faqList.add(new FaqModel(orderList.get(i).getName(),orderList.get(i).getQuestion(), orderList.get(i).getSolution()));
        }
        for(int i = 0; i<deliveryList.size(); i++){
            faqList.add(new FaqModel(deliveryList.get(i).getName(),deliveryList.get(i).getQuestion(), deliveryList.get(i).getSolution()));
        }
        for(int i = 0; i<returnList.size(); i++){
            faqList.add(new FaqModel(returnList.get(i).getName(),returnList.get(i).getQuestion(), returnList.get(i).getSolution()));
        }
        for(int i = 0; i<accountList.size(); i++){
            faqList.add(new FaqModel(accountList.get(i).getName(),accountList.get(i).getQuestion(), accountList.get(i).getSolution()));
        }
        for(int i = 0; i<paymentList.size(); i++){
            faqList.add(new FaqModel(paymentList.get(i).getName(),paymentList.get(i).getQuestion(), paymentList.get(i).getSolution()));
        }
        for(int i = 0; i<productList.size(); i++){
            faqList.add(new FaqModel(productList.get(i).getName(),productList.get(i).getQuestion(), productList.get(i).getSolution()));
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FaqAdapter(this, faqList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        for (int i = 0; i < faqList.size(); i++) {
            if (title.equals(faqList.get(i).getHeader())) {
                position = i;
                break;
            }
        }
        linearLayoutManager.scrollToPosition(position);

    }
}