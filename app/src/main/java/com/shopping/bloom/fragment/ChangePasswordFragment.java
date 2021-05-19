package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.databinding.FragmentChangePasswordBinding;
import com.shopping.bloom.utils.DebouncedOnClickListener;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnChangePassword.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                checkInputFields();
            }
        });

    }

    private void checkInputFields() {
        if (binding.txtCurrentPassword.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), "Empty Field Detected", Toast.LENGTH_SHORT).show();
        else if (binding.txtNewPassword.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), "Empty Field Detected", Toast.LENGTH_SHORT).show();
        else if (binding.txtConfirmPassword.getText().toString().trim().isEmpty())
            Toast.makeText(getContext(), "Empty Field Detected", Toast.LENGTH_SHORT).show();
        else {

            String newPassword = binding.txtNewPassword.getText().toString().trim();
            String confirmPassword = binding.txtConfirmPassword.getText().toString().trim();

            if (!newPassword.equals(confirmPassword))
                Toast.makeText(getContext(), "New Password and Confirm Password does not match", Toast.LENGTH_LONG).show();

            else {

            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}