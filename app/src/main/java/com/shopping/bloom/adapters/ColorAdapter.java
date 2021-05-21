package com.shopping.bloom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.faq.ColorModel;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.HashMap;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private final Context context;
    private List<String> colorList;
    private int pos = -1;
    private boolean clickable;
    private String selectedColor;
    private HashMap<String, String> colorMap;

    public ColorAdapter(Context context, List<String> colorList) {
        this.context = context;
        this.colorList = colorList;
        colorMap = new HashMap<>();
        fillColorHashMap(context);
    }

    public void setColorList(List<String> colorList, boolean clickable) {
        this.colorList = colorList;
        this.clickable = clickable;
        notifyDataSetChanged();
    }

    public void getSelectedColor(String selectedColor) {
        this.selectedColor = selectedColor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_color_button, parent, false);
        return new ViewHolder(view);
    }

    private void fillColorHashMap(Context context) {
        colorMap = new HashMap<>();
        List<ColorModel> colorModels = RemoteConfig.getColorPalletConfig(context).getColorPalletList();
        for (ColorModel colorModel : colorModels) {
            colorMap.put(colorModel.getName(), colorModel.getHexValue());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        String color = colorMap.get(colorList.get(position));
        Log.d("colorsize", "adapter colorsize = " + colorList.size());

        if (color == null) {
            System.out.println("Empty color");
        } else {
            holder.button.setBackgroundColor(Color.parseColor(color));
        }

        float radius11f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, context.getResources().getDisplayMetrics());
        float radius15f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, context.getResources().getDisplayMetrics());

        if (selectedColor != null) {
            for (int i = 0; i < colorList.size(); i++) {
                if (selectedColor.equals(colorList.get(i))) {
                    System.out.println("POSITION = " + i);
                    holder.cardView.setContentPadding(10, 10, 10, 10);
                    holder.cardView2.setRadius(radius11f);
                    holder.button.setChecked(false);
                    pos = i;
                }
            }
        }
        holder.button.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                if (holder.button.isChecked()) {

                    holder.cardView.setContentPadding(10, 10, 10, 10);
                    holder.cardView2.setRadius(radius11f);
                    if (context instanceof SingleProductActivity) {
                        ((SingleProductActivity) context).setViewPagerCurrentItem(position);
                    }
                    pos = position;

                } else {
                    holder.cardView.setContentPadding(0, 0, 0, 0);
                    if (context instanceof SingleProductActivity) {
                        ((SingleProductActivity) context).setViewPagerCurrentItem(-1);
                    }
                    pos = -1;
                }
                notifyDataSetChanged();
            }
        });


        if (pos == position) {
            holder.cardView.setContentPadding(10, 10, 10, 10);
            holder.cardView2.setRadius(radius11f);
            holder.button.setChecked(true);
        } else {
            holder.cardView.setContentPadding(0, 0, 0, 0);
            holder.cardView2.setRadius(radius15f);
            holder.button.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        if (this.colorList != null) {
            return this.colorList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox button;
        CardView cardView, cardView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.colorButton);
            cardView = itemView.findViewById(R.id.cardView);
            cardView2 = itemView.findViewById(R.id.cardView2);
//            float radius11f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11f, context.getResources().getDisplayMetrics());
//            if(selectedColor != null){
//                for(int i = 0; i<colorList.size(); i++){
//                    if(selectedColor.equals(colorList.get(i))){
//                        System.out.println("POSITION = " + i);
//                        cardView.setContentPadding(10, 10, 10, 10);
//                        cardView2.setRadius(radius11f);
//                        button.setChecked(true);
//                    }
//                }
//            }

//            if(selectedColor != null) {
//                System.out.println("SELECTED COLOR = " + selectedColor);
//                if (selectedColor.equals(color)) {
//                    holder.button.setChecked(true);
//                    holder.cardView.setContentPadding(10, 10, 10, 10);
//                    holder.cardView2.setRadius(radius11f);
//                    pos = position;
//                }
//            }
        }

    }

}
