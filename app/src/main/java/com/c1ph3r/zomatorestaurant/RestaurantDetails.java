package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRestaurantDetailsBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantDetails extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding RESTAURANT_DETAILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAURANT_DETAILS = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        View view = RESTAURANT_DETAILS.getRoot();
        setContentView(view);

        RestaurantUserDetails RestaurantDetails = new RestaurantUserDetails();

        FirebaseFirestore RestaurantDB = FirebaseFirestore.getInstance();


    }
}