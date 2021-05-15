package com.shopping.bloom.restService.callback;

import com.shopping.bloom.model.AddressDataResponse;

public interface AddressClickListener {
    void onAddressClicked(AddressDataResponse address);
    void onAddressUpdate(AddressDataResponse address);
    void onAddressDelete(AddressDataResponse address);
    void setAsDefault(AddressDataResponse address);
}
