package com.shopping.bloom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.shopping.bloom.R;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.ColorImageArray;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.faq.ColorModel;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ProductViewHolder> {

    private static final String TAG = AllProductsAdapter.class.getName();
    private List<Product> productList;
    private final WishListListener wishListListener;
    private final Context context;
    private Map<String, String> colorMap;
    static String DEFAULT_BORDER_COLOR = "#FFFFFF";
    static int COLOR_SELECTOR_ICON_SIZE = 80;
    static int DEFAULT_BORDER_RADIUS = 8;

    public AllProductsAdapter(Context context, WishListListener wishListListener) {
        this.context = context;
        this.wishListListener = wishListListener;
        productList = new ArrayList<>();
        fillColorHashMap(context);
    }

    //Get the Color pallet data from the Remote config
    private void fillColorHashMap(Context context) {
        colorMap = new HashMap<>();
        List<ColorModel> colorModels = RemoteConfig.getColorPalletConfig(context).getColorPalletList();
        Log.d(TAG, "fillColorHashMap: " + colorModels.toString());
        for (ColorModel colorModel : colorModels) {
            colorMap.put(colorModel.getName(), colorModel.getHexValue());
        }
    }

    public void updateList(List<Product> products) {
        if (products == null) return;
        this.productList = products;
        notifyDataSetChanged();
    }

    public void addProductList(List<Product> products) {
        if (products == null) return;
        int position = productList.size();
        this.productList.addAll(products);
        notifyItemRangeInserted(position, products.size());
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view_category, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItemAt(position);

        holder.setUpData(context, product, colorMap);

        holder.viewFavorites.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                boolean isLiked = product.isInUserWishList();
                wishListListener.updateWishList(position, !isLiked);
            }
        });

        holder.imgProductImage.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                wishListListener.productClicked(product);
            }
        });
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        View viewFavorites;
        TextView tvPrice;
        LinearLayout parentColorsLayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            viewFavorites = itemView.findViewById(R.id.viewFavorites);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            parentColorsLayout = itemView.findViewById(R.id.llColorOptions);
        }

        public void setUpData(Context context, Product product, Map<String, String> colorMap) {
            //1. show primary image
            //2. Change wishListIcon (if product is previously added to wishList)
            //3. Populate color option
            String imageURL = Const.GET_BASE_URL + product.getPrimary_image();
            CommonUtils.loadImageWithGlide(context, imageURL, imgProductImage, true);
            tvPrice.setText(CommonUtils.getSignedAmount(product.getPrice()));

            setLiked(context, product.isInUserWishList());

            // Populate the color array and pass the callbackListener to change the Image
            List<ColorImageArray> colors = product.getColorsImageArray();
            if (colors != null && !colors.isEmpty()) {
                addColors(context, colors, colorMap, (imageUrl -> changeImage(context, imageUrl)));
            } else {
                parentColorsLayout.removeAllViews();
                parentColorsLayout.removeAllViewsInLayout();
            }
        }

        public void changeImage(Context context, String imagePath) {
            String imageURL = Const.GET_BASE_URL + imagePath;
            CommonUtils.loadImageWithGlide(context, imageURL, imgProductImage, true);
        }

        public void setLiked(Context context, boolean isLiked) {
            if (isLiked) {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wishlist_background));
            } else {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wishlist_product));
            }
        }

        /*
         *  @params colors is the list of ColorImageArray which contain color, imageURL
         *  @params callback is used to change the image upon clicking the color
         */
        public void addColors(Context context, List<ColorImageArray> colors, Map<String, String> colorMap, ColorClickListener callback) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parentColorsLayout.removeAllViewsInLayout();
            parentColorsLayout.removeAllViews();
            for (ColorImageArray color : colors) {
                if (!colorMap.containsKey(color.getColor())) {
                    String colorNotFound = "COLOR NOT found" + color.getColor();
                    Log.d(TAG, "addColors: " + colorNotFound);
                    FirebaseCrashlytics.getInstance().log(colorNotFound);
                    continue;
                }
                CircleImageView colorCircle = new CircleImageView(context);
                params.rightMargin = 28;
                colorCircle.setLayoutParams(params);
                colorCircle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bg_circle_selector));
                try{
                    colorCircle.setColorFilter(Color.parseColor(colorMap.get(color.getColor())), PorterDuff.Mode.SRC_ATOP);
                } catch (NullPointerException nullPointerException) {
                    Log.d(TAG, "addColors: unable to parse the color");
                    FirebaseCrashlytics.getInstance().log("Unable to parse the color " + colorMap.get(color.getColor()));
                }
                colorCircle.getLayoutParams().height = COLOR_SELECTOR_ICON_SIZE;
                colorCircle.getLayoutParams().width = COLOR_SELECTOR_ICON_SIZE;
                colorCircle.setBorderWidth(DEFAULT_BORDER_RADIUS);
                colorCircle.setBorderColor(Color.parseColor(DEFAULT_BORDER_COLOR));
                colorCircle.setOnClickListener(new DebouncedOnClickListener(200) {
                    @Override
                    public void onDebouncedClick(View v) {
                        changeBackgroundColor(parentColorsLayout, color.getColor(), colors);
                        callback.onColorSelected(color.getImagePath());
                    }
                });
                parentColorsLayout.addView(colorCircle);
            }
        }

        /*
         *   Populate circular color options
         * */
        private void changeBackgroundColor(LinearLayout parentLayout,
                                           String colorClicked, List<ColorImageArray> colors) {
            /*
             *   return IFF
             *       colorArray is empty,
             *       childView count = 0,
             *       size of colorArray is different from the childViewCount
             * */
            int childViewCount = parentLayout.getChildCount();
            if (childViewCount == 0 || colors == null ||
                    colors.isEmpty() || colors.size() != childViewCount) return;

            //Border color
            String BORDER_COLOR_LITE = "#BBBBBB";
            String BORDER_COLOR_DARK = "#888888";

            for (int i = 0; i < childViewCount; i++) {
                View view = parentLayout.getChildAt(i);
                CircleImageView colorCircle = (CircleImageView) view;
                String colorName = colors.get(i).getColor();
                if (colorClicked.equals(colorName)) {
                    if (colorClicked.equals("White")) {
                        colorCircle.setBorderColor(Color.parseColor(BORDER_COLOR_DARK));
                    } else {
                        colorCircle.setBorderColor(Color.parseColor(BORDER_COLOR_LITE));
                    }
                } else {
                    colorCircle.setBorderColor(Color.parseColor(DEFAULT_BORDER_COLOR));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private interface ColorClickListener {
        void onColorSelected(String color);
    }

    private List<String> getAllColors(String available_colors) {
        Log.d(TAG, "getAllColors: " + available_colors.toString());
        if (available_colors.isEmpty()) {
            Log.d(TAG, "getAllColors: EMPTY COLOR ARRAY");
            return null;
        }
        List<String> colors;
        colors = Arrays.asList(available_colors.split(","));
        return colors;
    }

    public void updateItem(int position, boolean isAdded) {
        Log.d(TAG, "updateItem: " + isAdded);
        productList.get(position).setInUserWishList(isAdded);
        notifyItemChanged(position);
    }

    public Product getItemAt(int position) {
        if (position < 0 || position > productList.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return productList.get(position);
    }

}
