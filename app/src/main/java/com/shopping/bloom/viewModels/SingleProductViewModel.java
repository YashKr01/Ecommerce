package com.shopping.bloom.viewModels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shopping.bloom.App;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PinCodeResponse;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.AddToCartCallback;
import com.shopping.bloom.restService.response.GetProductsResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.restService.response.SingleProductResponse;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleProductViewModel extends ViewModel {

    private final MutableLiveData<SingleProductDataResponse> mutableLiveData;
    private final MutableLiveData<LoginResponseModel> loginResponseModelMutableLiveData;
    private final MutableLiveData<List<Product>> randomImageDataResponseMutableLiveData;
    private final MutableLiveData<PinCodeResponse> pinCodeResponseMutableLiveData;
    private final String token;

    public SingleProductViewModel() {

        LoginManager loginManager = new LoginManager(App.getContext());
        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }
        mutableLiveData = new MutableLiveData<>();
        loginResponseModelMutableLiveData = new MutableLiveData<>();
        randomImageDataResponseMutableLiveData = new MutableLiveData<>();
        pinCodeResponseMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Product>> getRandomImageDataResponseMutableLiveData() {
        return randomImageDataResponseMutableLiveData;
    }

    public MutableLiveData<LoginResponseModel> getLoginResponseModelMutableLiveData() {
        return loginResponseModelMutableLiveData;
    }

    public MutableLiveData<SingleProductDataResponse> getMutableLiveData() {
        return mutableLiveData;
    }

    public MutableLiveData<PinCodeResponse> getPinCodeResponseMutableLiveData() {
        return pinCodeResponseMutableLiveData;
    }

    public void makeApiCall(int id, Application application) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<SingleProductResponse> call = apiService.getSingleProduct("Bearer " + token, id);
        call.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.postValue(response.body().getSingleProductDataResponse());
                }
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }

    public void makeApiCallCreateUserActivity(String product_id, int category_id, Application application) {


        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<LoginResponseModel> call;
        call = apiService.createUserActivity(product_id, category_id, "Bearer " + token);

        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                if (response.body() == null) {
                    System.out.println("NO DATA");
                } else {
                    loginResponseModelMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                loginResponseModelMutableLiveData.postValue(null);
            }
        });
    }

    public void makeApiCallRandomImage(int limit, int pageNo, Application application) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<GetProductsResponse> call = apiService.getRandomProducts("Bearer " + token, pageNo, limit);
        call.enqueue(new Callback<GetProductsResponse>() {
            @Override
            public void onResponse(Call<GetProductsResponse> call, Response<GetProductsResponse> response) {
                if (response.body() == null) {
                    System.out.println("NO DATA");
                } else {
                    randomImageDataResponseMutableLiveData.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GetProductsResponse> call, Throwable t) {
                randomImageDataResponseMutableLiveData.postValue(null);
            }
        });
    }

    public void addToShoppingBag(CartItem cartItem, String quantity, AddToCartCallback cartCallback) {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            List<CartItem> cartItems = EcommerceDatabase.getInstance().cartItemDao()
                    .getAllProductWith(cartItem.getParentId(), cartItem.getChildId());
            if (cartItems == null || cartItems.isEmpty()) {
                EcommerceDatabase.getInstance().cartItemDao().addToCart(cartItem);
                cartCallback.onAdded(1);
            } else {
                if (cartItems.get(0).getQuantity() < Integer.parseInt(quantity)) {
                    EcommerceDatabase.getInstance().cartItemDao()
                            .incrementQuantity(cartItem.getParentId(), cartItem.getChildId());
                    cartCallback.onAdded(cartItems.get(0).getQuantity() + 1);
                } else {
                    cartCallback.onItemLimitReached(cartItems.get(0).getQuantity());
                }
            }
        });
    }

    public LiveData<Integer> getCartSize() {
        return EcommerceDatabase.getInstance().cartItemDao().changeCartIcon();
    }

    public void checkPinCode(String pinCode, Application application) {

        ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
        Call<PinCodeResponse> call = apiService.checkPinCode("Bearer " + token, pinCode);
        call.enqueue(new Callback<PinCodeResponse>() {
            @Override
            public void onResponse(Call<PinCodeResponse> call, Response<PinCodeResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        pinCodeResponseMutableLiveData.postValue(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<PinCodeResponse> call, Throwable t) {
                randomImageDataResponseMutableLiveData.postValue(null);
            }
        });
    }
}
