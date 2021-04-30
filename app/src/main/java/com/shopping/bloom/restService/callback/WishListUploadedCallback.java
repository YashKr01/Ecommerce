package com.shopping.bloom.restService.callback;

public interface WishListUploadedCallback {
    void onUploadSuccessful();
    void onUploadFailed(int errorCode, String message);
}
