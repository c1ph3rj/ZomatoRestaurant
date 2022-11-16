package com.c1ph3r.zomatorestaurant.Controller;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.c1ph3r.zomatorestaurant.R;
import com.google.android.material.button.MaterialButton;

public class dialogBox {
    public static void alertTheUser(String Title, String Message, Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        TextView TITLE, MESSAGE;
        TITLE = dialog.findViewById(R.id.TITLE);
        TITLE.setText(Title);
        MESSAGE = dialog.findViewById(R.id.MESSAGE);
        MESSAGE.setText(Message);
        MaterialButton OK;
        OK = dialog.findViewById(R.id.Ok);
        OK.setOnClickListener(OnClickOk -> dialog.dismiss());
        dialog.show();
    }
}
