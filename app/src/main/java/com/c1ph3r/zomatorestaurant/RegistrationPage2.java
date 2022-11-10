package com.c1ph3r.zomatorestaurant;

import android.Manifest;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class RegistrationPage2 extends Fragment {


    public RegistrationPage2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_page2, container, false);

        if(view != null){
            shouldShowRequestPermissionRationale(Manifest.permission_group.STORAGE);
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission_group.STORAGE)){
                Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }
}