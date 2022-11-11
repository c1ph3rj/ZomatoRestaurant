package com.c1ph3r.zomatorestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.c1ph3r.zomatorestaurant.Adapter.RegistrationProcess;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegisterPage1Binding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegistrationPage2Binding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class RegisterRestaurant extends AppCompatActivity {

    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;
    private FragmentRegisterPage1Binding PAGE1;
    FragmentRegistrationPage2Binding PAGE2;
    RegisterPage1 restaurantDetails;
    RegistrationPage2 documentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);
        restaurantDetails = new RegisterPage1();
        documentDetails = new RegistrationPage2();

        RegistrationProcess process = new RegistrationProcess(RegisterRestaurant.this);
        process.addFragment(restaurantDetails);
        process.addFragment(documentDetails);


        NEW_RESTAURANT.RegistrationProcess.setAdapter(process);
        NEW_RESTAURANT.RegistrationProcess.setUserInputEnabled(false);


        NEW_RESTAURANT.NextButton.setOnClickListener( OnClickNext -> {
            switch (NEW_RESTAURANT.RegistrationProcess.getCurrentItem()){
                case 0:
                    if(checkInputs())
                        nextPage();
                    break;
                case 1:
                    if(CheckDocumentValues())
                        submitValues();
                    else
                        alertTheUser("Upload Document Image to continue!", "Upload a valid FSSAI Document Image to continue");
            }
        });


        NEW_RESTAURANT.RegistrationProcess.setCurrentItem(1);
    }

    private void submitValues() {
        FirebaseFirestore restaurantDB = FirebaseFirestore.getInstance();
        uploadImage();
        String mobileNumber = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        if(mobileNumber != null){
            DocumentReference restaurantMerchant = restaurantDB.collection("restaurantDataBase").document(mobileNumber);
        }
    }

    private String uploadImage() {
        String url = null;
        StorageReference documentsDB = FirebaseStorage.getInstance().getReference("restaurantDocuments/")
                .child(UUID.randomUUID().toString());
        UploadTask uploadImage  = documentsDB.putFile(documentDetails.imageUri);
                uploadImage.addOnSuccessListener(
                        taskSnapshot -> {
                            // Image uploaded successfully
                            Toast.makeText(RegisterRestaurant.this,
                                            "Document uploaded Successfully",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }).addOnFailureListener(e -> {
                    // Error, Image not uploaded
                    Toast
                            .makeText(RegisterRestaurant.this,
                                    "Failed To Upload Image Try Again" + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return null;
                    }
                });

        return url;
    }

    private boolean CheckDocumentValues() {
        View view = documentDetails.getView();
        PAGE2 = FragmentRegistrationPage2Binding.bind(view);
        return PAGE2.PreviewDocument.getDrawable() != null;
    }

    private void nextPage() {
        NEW_RESTAURANT.RegistrationProcess.setCurrentItem(1);
        NEW_RESTAURANT.NextButton.setText("Submit");
    }

    private boolean checkInputs() {
        View view = restaurantDetails.getView();
        if(view != null){
            PAGE1 = FragmentRegisterPage1Binding.bind(view);
            if(!PAGE1.OwnerName.getText().toString().matches( "^[A-Za-z]\\w{5,28}$")) {
                alertTheUser("Enter a valid UserName!", "UserName must starts with alphabets(Aa-Zz) and minimum 7 not more than 28 words, and Do not contain Space.");
                return false;
            }

            if(PAGE1.RestaurantName.getText().toString().isEmpty()) {
                alertTheUser("Enter a valid Restaurant Name!", "Restaurant Name cannot be empty.");
                return false;
            }

            if(!PAGE1.GSTNo.getText().toString().isEmpty()){
                if(!PAGE1.GSTNo.getText().toString().matches("^[1-9][0-9]{14}$")) {
                    alertTheUser("Enter a valid GST Number!", "GST Number you have entered is not valid");
                    return false;
                }
            }
            if(!PAGE1.FSSAINumber.getText().toString().matches("^[A-Za-z1-9][A-Za-z0-9]{14}$")){
                alertTheUser("Enter a valid FSSAINumber!", "FSSAINumber you have entered is not valid");
                return false;
            }

            if(!PAGE1.PanCardNumber.getText().toString().matches("^[A-Za-z1-9][A-Za-z0-9]{9}$")){
                alertTheUser("Enter a valid PanCard Number!", "PanCard Number you have entered is not valid");
                return false;
            }

            if(!PAGE1.ContactNumber.getText().toString().matches("^[6-9][0-9]{9}$")){
                alertTheUser("Enter a valid Contact Number!", "Contact Number you have entered is not valid");
                return false;
            }

            return true;
        }else
            return false;
        }


    void alertTheUser(String Title, String Message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        TextView TITLE, MESSAGE;
        Button Ok;
        TITLE = dialog.findViewById(R.id.TITLE);
        MESSAGE = dialog.findViewById(R.id.MESSAGE);
        Ok = dialog.findViewById(R.id.Ok);

        TITLE.setText(Title);
        MESSAGE.setText(Message);
        Ok.setOnClickListener(onClickOk ->dialog.cancel());

        dialog.show();

    }
}