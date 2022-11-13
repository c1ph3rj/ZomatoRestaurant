package com.c1ph3r.zomatorestaurant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterPage1 extends Fragment {

    String mobileNumber;
    public RegisterPage1(String mobileNumber) {
      this.mobileNumber = mobileNumber;
    }

    public RegisterPage1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_page1, container, false);

        if(view != null){
            TextInputEditText contactNumber = view.findViewById(R.id.ContactNumber);

            if(mobileNumber != null) {
               warnTheUser();
                contactNumber.setText(mobileNumber.replace(getString(R.string.CountryCode), ""));
                contactNumber.setEnabled(false);
            }

        }

        return view;
    }

    private void warnTheUser() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog);
        TextView TITLE, MESSAGE;
        TITLE = dialog.findViewById(R.id.TITLE);
        TITLE.setText("Register your Restaurant!");
        MESSAGE = dialog.findViewById(R.id.MESSAGE);
        MESSAGE.setText("You do not have registered your restaurant yet click ok to start your registration process.");
        MaterialButton OK;
        OK = dialog.findViewById(R.id.Ok);
        OK.setOnClickListener(OnClickOk -> dialog.dismiss());
        dialog.show();
    }
}