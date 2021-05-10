package com.shopping.bloom.restService.callback;

import com.shopping.bloom.restService.response.GetCartValueResponse;

public interface CartValueCallback {

    void onSuccess(GetCartValueResponse response);
    void onFailed(int errorCode, String message);

}
