package com.c1ph3r.zomatorestaurant;

import android.Manifest;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c1ph3r.zomatorestaurant.databinding.FragmentRegistrationPage3Binding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MapStyleOptions;


public class RegistrationPage3 extends Fragment implements OnMapReadyCallback {
    View view;
    private FragmentRegistrationPage3Binding GET_ADDRESS;

    public RegistrationPage3(String mobileNumber) {

    }

    public RegistrationPage3(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registration_page3, container, false);
        GET_ADDRESS = FragmentRegistrationPage3Binding.bind(view);


        if(view != null){
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            GET_ADDRESS.GMap.getMapAsync(this);
            GET_ADDRESS.GMap.onCreate(savedInstanceState);

        }

        return view;
    }

    void getLocationPermission(){
        requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }


    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            result -> {
                if(result){
                    System.out.println("PermissionGranted");
                }else{
                    System.out.println("PermissionDenied");
                    getLocationPermission();
                }
            });

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onStart() {
        super.onStart();
        GET_ADDRESS.GMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        GET_ADDRESS.GMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        GET_ADDRESS.GMap.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
        GET_ADDRESS.GMap.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GET_ADDRESS.GMap.onDestroy();
    }
}