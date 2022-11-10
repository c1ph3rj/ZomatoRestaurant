package com.c1ph3r.zomatorestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c1ph3r.zomatorestaurant.Adapter.RegistrationProcess;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegisterPage1Binding;

public class RegisterRestaurant extends AppCompatActivity {

    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;
    private FragmentRegisterPage1Binding PAGE1;
    RegisterPage1 restaurantDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);
        restaurantDetails = new RegisterPage1();


        RegistrationProcess process = new RegistrationProcess(RegisterRestaurant.this);
        process.addFragment(restaurantDetails);
        process.addFragment(new RegistrationPage2());

        NEW_RESTAURANT.RegistrationProcess.setAdapter(process);
        NEW_RESTAURANT.RegistrationProcess.setUserInputEnabled(false);


        NEW_RESTAURANT.NextButton.setOnClickListener( OnClickNext -> {
            switch (NEW_RESTAURANT.RegistrationProcess.getCurrentItem()){
                case 0:
                    checkInputs();
                    break;
            }
        });
    }

    private void checkInputs() {
        View view = restaurantDetails.getView();
        if(view != null){
            PAGE1 = FragmentRegisterPage1Binding.bind(view);
            if(PAGE1.OwnerName.getText().toString().matches( "^[A-Za-z]\\w{5,28}$")){
                System.out.println(PAGE1.OwnerName.getText().toString());
            }else
               alertTheUser("Enter a valid UserName!", "UserName must starts with alphabets(Aa-Zz) and not more than 28 words.")
                       .show();
        }
    }


    Dialog alertTheUser(String Title, String Message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        TextView TITLE, MESSAGE;
        Button Ok;
        TITLE = dialog.findViewById(R.id.TITLE);
        MESSAGE = dialog.findViewById(R.id.MESSAGE);
        Ok = dialog.findViewById(R.id.Ok);

        TITLE.setText(Title);
        MESSAGE.setText(Message);
        Ok.setOnClickListener(onClickOk -> dialog.cancel());
        dialog.create();

        return dialog;

    }
}