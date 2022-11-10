package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.c1ph3r.zomatorestaurant.Adapter.RegistrationProcess;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;
import com.google.android.material.button.MaterialButton;

public class RegisterRestaurant extends AppCompatActivity {

    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);


        RegistrationProcess process = new RegistrationProcess(RegisterRestaurant.this);
        process.addFragment(new RegisterPage1());
        process.addFragment(new RegistrationPage2());

        NEW_RESTAURANT.RegistrationProcess.setAdapter(process);
        NEW_RESTAURANT.RegistrationProcess.setUserInputEnabled(false);


        NEW_RESTAURANT.NextButton.setOnClickListener( OnClickNext -> {
            switch (NEW_RESTAURANT.RegistrationProcess.getCurrentItem()){
                case 0:
                    NEW_RESTAURANT.RegistrationProcess.setCurrentItem(1);
                    NEW_RESTAURANT.NextButton.setText("Submit");
                    break;
            }
        });
    }
}