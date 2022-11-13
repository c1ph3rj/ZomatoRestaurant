package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class LoadingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding_screen);

        if(FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivity(new Intent(LoadingScreen.this, MainActivity.class));
        else{
            FirebaseFirestore FireStore = FirebaseFirestore.getInstance();
            String mobileNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            assert mobileNumber != null;
            DocumentReference RestaurantDB = FireStore.collection(getString(R.string.RESTAURANT_DB)).document(mobileNumber);
            RestaurantDB.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot restaurantDetails) {
                    if(restaurantDetails.exists()){
                        RestaurantUserDetails restaurant = restaurantDetails.toObject(RestaurantUserDetails.class);
                        assert restaurant != null;
                        System.out.println(restaurant.getTopFoodImages().size() == 0);
                        if(Objects.equals(restaurant.getRestaurantName(), ""))
                            startActivity(new Intent(LoadingScreen.this, RegisterRestaurant.class));
                        else if(restaurant.getTopFoodImages().size() ==0) {
                            startActivity(new Intent(LoadingScreen.this, RestaurantDetails.class));
                        }else
                            startActivity(new Intent(LoadingScreen.this, Dashboard.class));
                        finish();
                    }else {
                        Intent intent = new Intent(LoadingScreen.this, RegisterRestaurant.class);
                        intent.putExtra("mobileNumber", mobileNumber);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}