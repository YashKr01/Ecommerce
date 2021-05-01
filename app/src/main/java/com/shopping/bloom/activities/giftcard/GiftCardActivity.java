package com.shopping.bloom.activities.giftcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.giftcard.GiftCardAdapter;
import com.shopping.bloom.databinding.ActivityGiftCardBinding;
import com.shopping.bloom.model.giftcard.GiftCard;

import java.util.ArrayList;
import java.util.List;

public class GiftCardActivity extends AppCompatActivity {

    private ActivityGiftCardBinding binding;
    private GiftCardAdapter adapter;
    private List<GiftCard> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGiftCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        adapter = new GiftCardAdapter(list, this);
        binding.giftCardRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.giftCardRecyclerView.setAdapter(adapter);

        list.add(new GiftCard("10000","Validity 3/16/2021"));
        list.add(new GiftCard("10000","Validity 3/16/2021"));
        list.add(new GiftCard("10000","Validity 3/16/2021"));
        list.add(new GiftCard("10000","Validity 3/16/2021"));


    }
}