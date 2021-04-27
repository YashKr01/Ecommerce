package com.shopping.bloom.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewKt;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.App;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.UpdateAddressActivity;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.response.AddressResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressDataResponse> addressList;
    int lastSelectedPosition = -1;
    Application application;

    public AddressAdapter(Context context, List<AddressDataResponse> addressList, Application application) {
        this.context = context;
        this.addressList = addressList;
        this.application = application;
    }

    public void setAddressList(List<AddressDataResponse> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address, parent, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressDataResponse address = addressList.get(position);
        holder.nameTextView.setText(address.getAddress_name());
        holder.addressLine1TextView.setText(address.getAddress_line_1());
        holder.addressLine2TextView.setText(address.getPincode());
        holder.cityTextView.setText(address.getCity());
        holder.numberTextView.setText(address.getContact_number());

        LoginManager loginManager = new LoginManager(App.getContext());
        String token;

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        holder.imageButton.setOnClickListener(v -> {
            ApiInterface apiService = RetrofitBuilder.getInstance(application).retrofit.create(ApiInterface.class);
            Call<LoginResponseModel> call = apiService.deleteAddress(address.getId(), "Bearer " + token);
            call.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        addressList.remove(position);
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                }
            });
        });

        holder.updateImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateAddressActivity.class);
            intent.putExtra("addressName", address.getAddress_name());
            intent.putExtra("addressLine", address.getAddress_line_1());
            intent.putExtra("city", address.getCity());
            intent.putExtra("pinCode", address.getPincode());
            intent.putExtra("number", address.getContact_number());
            intent.putExtra("is_primary", address.getIs_primary());
            intent.putExtra("id", address.getId());
            context.startActivity(intent);
        });

        holder.radioButton.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        if (this.addressList != null) {
            return this.addressList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressLine1TextView,
                addressLine2TextView, cityTextView, numberTextView;
        ImageButton imageButton, updateImageButton;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            addressLine1TextView = itemView.findViewById(R.id.address1TextView);
            addressLine2TextView = itemView.findViewById(R.id.address2TextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);

            imageButton = itemView.findViewById(R.id.deleteButton);
            updateImageButton = itemView.findViewById(R.id.updateAddress);
            radioButton = itemView.findViewById(R.id.selectRadioButton);

            radioButton.setOnClickListener(v -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();

                Toast.makeText(context, nameTextView.getText(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
