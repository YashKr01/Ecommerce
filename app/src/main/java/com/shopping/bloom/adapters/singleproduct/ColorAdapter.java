package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private Context context;
    private List<String> colorList;
    private int pos = -1;
    private int check = 1;
    private boolean clickable;
    private HashMap<String, String> colorMap;

    public ColorAdapter(Context context, List<String> colorList) {
        this.context = context;
        this.colorList = colorList;
        colorMap = new HashMap<>();
        fillColorHashMap();
    }

    public void setColorList(List<String> colorList, boolean clickable) {
        this.colorList = colorList;
        this.clickable = clickable;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_color_button, parent, false);
        return new ViewHolder(view);
    }

    private void fillColorHashMap() {
        colorMap.put("Blue", "#2A93DF");
        colorMap.put("Red", "#FD5353");
        colorMap.put("Black", "#000000");
        colorMap.put("Brown", "#A0552B");
        colorMap.put("Green", "#11DF3E");
        colorMap.put("Yellow", "#FAE423");
        colorMap.put("White", "#E6E6E6");
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.ViewHolder holder, int position) {
        String color = colorMap.get(colorList.get(position));
        System.out.println(color);
        if (color == null) {
            System.out.println("Empty color");
        } else {
            holder.button.setBackgroundColor(Color.parseColor(color));
        }

        if (clickable) {
            holder.button.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    if (holder.button.isChecked()) {
                        holder.cardView.setContentPadding(10, 10, 10, 10);
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

        }
        if (pos == position) {
            holder.cardView.setContentPadding(10, 10, 10, 10);
            holder.button.setChecked(true);
        } else {
            holder.cardView.setContentPadding(0, 0, 0, 0);
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
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.colorButton);
            cardView = itemView.findViewById(R.id.cardView);

        }

    }

}
