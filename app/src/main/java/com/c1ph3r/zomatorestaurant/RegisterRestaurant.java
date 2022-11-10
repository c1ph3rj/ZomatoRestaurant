package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;

public class RegisterRestaurant extends AppCompatActivity {

    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);
    }
}