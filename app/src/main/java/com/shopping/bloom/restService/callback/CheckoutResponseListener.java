package com.shopping.bloom.restService.callback;

import com.shopping.bloom.restService.response.GetCheckoutResponse;

public interface CheckoutResponseListener {
    void onSuccess(GetCheckoutResponse response);
    void onFailed(int errorCode, String errorMessage);
}
