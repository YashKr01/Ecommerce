package com.shopping.bloom.viewModels.wishlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shopping.bloom.App;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.ProductIds;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.model.wishlist.WishList;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.PutWishListRequest;
import com.shopping.bloom.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shopping.bloom.database.repository.ProductRepository.getToken;
import static com.shopping.bloom.database.repository.ProductRepository.join;

public class WishListViewModel extends AndroidViewModel {

    private Application application;

    public WishListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public MutableLiveData<List<WishListData>> getWishList(String pageNo, String limit) {

        ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();
        MutableLiveData<List<WishListData>> data = new MutableLiveData<>();

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) token = loginManager.gettoken();
        else token = loginManager.getGuest_token();

        Log.d("USERTOKEN", "getWishList: " + token);

        apiInterface.getWishList(pageNo, limit, "Bearer " + token).enqueue(new Callback<WishList>() {
            @Override
            public void onResponse(Call<WishList> call, Response<WishList> response) {

                String token = getToken();

                if (response.isSuccessful() && response.body() != null) {
                    List<WishListItem> wishListItems = new ArrayList<>();

                    if (response.body().getWishListData() != null) {

                        data.postValue(response.body().getWishListData());

                        // creating list of product ids for inserting in db
                        for (WishListData wishListData : response.body().getWishListData()) {
                            WishListItem item = new WishListItem(wishListData.getId() + "", token);
                            wishListItems.add(item);
                        }

                    }

                    // inserting each product in db
                    EcommerceDatabase.databaseWriteExecutor.execute(() ->
                            EcommerceDatabase.getInstance().wishListProductDao().addAllItems(wishListItems)
                    );

                } else {
                    data.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<WishList> call, Throwable t) {
                Log.d("WISHLIST", "onFailure: " + t.getMessage());
                data.postValue(null);
            }
        });

        return data;
    }

    public void deleteWishListProduct(String id) {
        EcommerceDatabase.databaseWriteExecutor.execute(() ->
                EcommerceDatabase.getInstance().wishListProductDao().deleteProductWithId(id)
        );
    }

    public void postRemainingItems() {

        EcommerceDatabase.databaseWriteExecutor.execute(() -> {

            ApiInterface apiInterface = RetrofitBuilder.getInstance(application).getApi();

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

    public void saveWishListOnServer(Call<PutWishListRequest> request) {

        if (request != null) {
            request.enqueue(new Callback<PutWishListRequest>() {
                @Override
                public void onResponse(Call<PutWishListRequest> call, Response<PutWishListRequest> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        Log.d("SAVE RESPONSE", "onResponse: ");
                        deleteAll();
                    }
                }

                @Override
                public void onFailure(Call<PutWishListRequest> call, Throwable t) {
                }
            });
        }
    }

    // deleting all list from database
    private void deleteAll() {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            EcommerceDatabase.getInstance().wishListProductDao().deleteAll();
        });
    }

}
