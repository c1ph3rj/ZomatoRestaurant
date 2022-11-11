package com.c1ph3r.zomatorestaurant.Controller;

import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.c1ph3r.zomatorestaurant.R;

public class AuthController {
    Dialog dialog;
    public boolean checkValues(RestaurantUserDetails userDetails, Dialog dialog){
        this.dialog = dialog;
        if (!userDetails.getOwnerName().matches("^[A-Za-z]\\w{5,28}$")) {
            alertTheUser("Enter a valid UserName!", "UserName must starts with alphabets(Aa-Zz) and minimum 7 not more than 28 words, and Do not contain Space.");
            return false;
        }

        if (userDetails.getRestaurantName().isEmpty()) {
            alertTheUser("Enter a valid Restaurant Name!", "Restaurant Name cannot be empty.");
            return false;
        }

        if (!userDetails.getGSTNumber().isEmpty()) {
            if (!userDetails.getGSTNumber().matches("^[1-9][0-9]{14}$")) {
                alertTheUser("Enter a valid GST Number!", "GST Number you have entered is not valid");
                return false;
            }
        }
        if (!userDetails.getFSSAINumber().matches("^[A-Za-z1-9][A-Za-z0-9]{14}$")) {
            alertTheUser("Enter a valid FSSAINumber!", "FSSAINumber you have entered is not valid");
            return false;
        }

        if (!userDetails.getPanCardNumber().matches("^[A-Za-z1-9][A-Za-z0-9]{9}$")) {
            alertTheUser("Enter a valid PanCard Number!", "PanCard Number you have entered is not valid");
            return false;
        }

        if (!userDetails.getMobileNumber().matches("^[6-9][0-9]{9}$")) {
            alertTheUser("Enter a valid Contact Number!", "Contact Number you have entered is not valid");
            return false;
        }

        return true;
    }


    void alertTheUser(String Title, String Message) {

        dialog.setContentView(R.layout.custom_dialog);
        TextView TITLE, MESSAGE;
        Button Ok;
        TITLE = dialog.findViewById(R.id.TITLE);
        MESSAGE = dialog.findViewById(R.id.MESSAGE);
        Ok = dialog.findViewById(R.id.Ok);

        TITLE.setText(Title);
        MESSAGE.setText(Message);
        Ok.setOnClickListener(onClickOk -> dialog.cancel());

        dialog.show();

    }
}
