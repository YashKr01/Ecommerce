package com.shopping.bloom.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewKt;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.App;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.MyAddressActivity;
import com.shopping.bloom.activities.UpdateAddressActivity;
import com.shopping.bloom.model.AddressDataResponse;
import com.shopping.bloom.restService.ApiInterface;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.AddressClickListener;
import com.shopping.bloom.restService.response.AddressResponse;
import com.shopping.bloom.restService.response.LoginResponseModel;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    List<AddressDataResponse> addressList;
    int lastSelectedPosition = -1;
    Application application;
    AddressClickListener mListener;

    public AddressAdapter(Context context, List<AddressDataResponse> addressList, Application application, AddressClickListener clickListener) {
        this.context = context;
        this.addressList = addressList;
        this.application = application;
        this.mListener = clickListener;
    }

    public void setAddressList(List<AddressDataResponse> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressDataResponse address = addressList.get(position);
        holder.nameTextView.setText(address.getAddress_name());
        holder.addressLine1TextView.setText(address.getAddress_line_1());
        holder.addressLine2TextView.setText(address.getPincode());
        holder.cityTextView.setText(address.getCity());
        holder.numberTextView.setText(address.getContact_number());

        holder.llAddressCard.setOnClickListener(view -> mListener.onAddressClicked(address));

        LoginManager loginManager = new LoginManager(App.getContext());

        holder.imageButton.setOnClickListener(v -> {
            if(NetworkCheck.isConnect(context)){
                mListener.onAddressDelete(address, position);
                lastSelectedPosition = 0;
                setLastSelectedPosition(lastSelectedPosition);
                notifyDataSetChanged();
            }else{
                ShowToast.showToast(context, "No Internet");
            }
        });

        holder.updateImageButton.setOnClickListener(v -> {
            mListener.onAddressUpdate(address);
        });

        holder.radioButton.setClickable(addressList.size() != 1);
        if (lastSelectedPosition != -1) {
            holder.radioButton.setChecked(lastSelectedPosition == position);
        } else {
            holder.radioButton.setChecked(address.getIs_primary().equals("1") || addressList.size() == 1);
            if (address.getIs_primary().equals("1")) {
                loginManager.setPrimary_address_id(address.getId());
                loginManager.setPrimaryAddress(address.getAddress_name() + "," + address.getAddress_line_1() + "," + address.getCity() + "," + address.getPincode() + "," + address.getContact_number());
                loginManager.setIs_primary_address_available(true);
            }
            if (addressList.size() == 1) {
                System.out.println("lastSelectedPosition = " + lastSelectedPosition);
                if (context instanceof MyAddressActivity) {
                    ((MyAddressActivity) context).getData(0);
                }
            }
        }
    }

    private void setLastSelectedPosition(int position) {
        if (context instanceof MyAddressActivity) {
            ((MyAddressActivity) context).getData(position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (addressList != null) {
            return addressList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, addressLine1TextView,
                addressLine2TextView, cityTextView, numberTextView;
        ImageButton imageButton, updateImageButton;
        RadioButton radioButton;
        LinearLayout llAddressCard;

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
            llAddressCard = itemView.findViewById(R.id.llAddressCard);

            radioButton.setOnClickListener(v -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
                if (context instanceof MyAddressActivity) {
                    ((MyAddressActivity) context).getData(lastSelectedPosition);
                }
            });


        }

    }
}
