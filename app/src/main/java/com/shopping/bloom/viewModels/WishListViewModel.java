package com.shopping.bloom.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductIds;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.PutWishListRequest;
import com.shopping.bloom.utils.Const;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.bloom.database.repository.ProductRepository.getToken;
import static com.shopping.bloom.database.repository.ProductRepository.join;

public class WishListViewModel extends AndroidViewModel {
    private static final String TAG = WishListViewModel.class.getName();
    private Application application;

    public WishListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void getWishList(int pageNo, int limit, ProductResponseListener listener) {
        String token = getToken();

        Call<GetProductsResponse> productsResponse = RetrofitBuilder.getInstance(application).getApi()
                .getWishList(token, pageNo, limit);

        if(productsResponse != null) {
            productsResponse.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if(!response.isSuccessful() || response.body() == null) {
                        listener.onFailure(-1, "Unsuccessful");
                        return;
                    }
                    if(response.code() == Const.SUCCESS) {
                        GetProductsResponse res = response.body();
                        if(res.getData() != null) {
                            insertItems(res.getData());     //TODO: INSERT ITEMS INTO WISHLIST???
                            listener.onSuccess(res.getData());
                        } else {
                            listener.onFailure(Const.SUCCESS, response.message());
                        }
                    } else {
                        listener.onFailure(response.code(), response.message());
                    }
                }

                @Override
                public void onFailure(Call<GetProductsResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    listener.onFailure(-1, t.getMessage());
                }
            });
        }
    }

    public void getRecommendationList(int limit, int page, ProductResponseListener responseListener) {
        String token = getToken();
        Call<GetProductsResponse> responseCall = RetrofitBuilder.getInstance(application).getApi()
                .getRandomProducts(token, page, limit);

        if(responseCall != null) {
            responseCall.enqueue(new Callback<GetProductsResponse>() {
                @Override
                public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                    if(!response.isSuccessful() || response.body() == null) {
                        responseListener.onFailure(-1, "Something went wrong");
                    }
                    if(response.code() == Const.SUCCESS) {
                        GetProductsResponse categoryResponse = response.body();
                        if(categoryResponse != null) {
                            responseListener.onSuccess(categoryResponse.getData());
                        } else {
                            responseListener.onFailure(200, "Invalid data received");
                        }
                    } else {
                        responseListener.onFailure(response.code(), "unrecognised error code");
                    }
                }

                @Override
                public void onFailure(Call<GetProductsResponse> call, Throwable t) {
                    responseListener.onFailure(-1, t.getMessage());
                }
            });
        }
    }

    public void deleteWishListProduct(String id) {
        EcommerceDatabase.databaseWriteExecutor.execute(() ->
                EcommerceDatabase.getInstance().wishListProductDao().deleteProductWithId(id)
        );
    }

    public void postRemainingItems() {
        ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            List<String> products = EcommerceDatabase.getInstance().wishListProductDao().getAllItem();
            if (products != null && products.size() > 0) {
                String authToken = getToken();
                String result = join(products);

                ProductIds res = new ProductIds(result);
                Call<PutWishListRequest> request = apiInterface.postUserWishList(authToken, res);
                saveWishListOnServer(request);
            } else {
                String authToken = getToken();
                ProductIds res = new ProductIds("");
                Call<PutWishListRequest> request = apiInterface.postUserWishList(authToken, res);
                saveWishListOnServer(request);
            }
        });
    }

    /*
     *  Insert all liked items into DB items into Database
     * */
    private void insertItems(List<Product> products) {
        if (products == null || products.size() == 0) return;

        List<WishListItem> wishListItems = new ArrayList<>();
        String token = getToken();
        for (Product product : products) {
            if (product.isInUserWishList()) {
                WishListItem wishListItem = new WishListItem(String.valueOf(product.getId()), token);
                wishListItems.add(wishListItem);
            }
        }
        //insert into db
        if(!wishListItems.isEmpty())
            EcommerceDatabase.databaseWriteExecutor.execute(() ->
                    EcommerceDatabase.getInstance().wishListProductDao().addAllItems(wishListItems));
    }

    public void saveWishListOnServer(Call<PutWishListRequest> request) {
        if (request != null) {
            request.enqueue(new Callback<PutWishListRequest>() {
                @Override
                public void onResponse(Call<PutWishListRequest> call, Response<PutWishListRequest> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        Log.d("SAVE RESPONSE", "onResponse: ");
                    }
                }

                @Override
                public void onFailure(Call<PutWishListRequest> call, Throwable t) {
                    Log.d(TAG, "onFailure: Something went wrong");
                }
            });
        }
    }

}
