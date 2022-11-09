package com.c1ph3r.zomatorestaurant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class OTP_Verification extends Fragment {


    private String mobileNumber;
    private FirebaseAuth auth;
    private String verificationID;

    public OTP_Verification() {
        // Required empty public constructor
    }

    public OTP_Verification(String mobileNumber, FirebaseAuth auth, String verificationID) {
        this.mobileNumber = mobileNumber;
        this.auth = auth;
        this.verificationID = verificationID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_t_p__verification, container, false);

        if(view != null){

        }

        return view;
    }
}