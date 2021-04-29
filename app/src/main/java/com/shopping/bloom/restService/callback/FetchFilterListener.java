package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.FilterArrayValues;

public interface FetchFilterListener {
    void fetchOnSuccess(FilterArrayValues filterValues);
    void fetchOnFailed(int errorCode, String message);
}
