package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding_screen);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivity(new Intent(LoadingScreen.this, MainActivity.class));
        else
            startActivity(new Intent(LoadingScreen.this, RegisterRestaurant.class));
    }
}