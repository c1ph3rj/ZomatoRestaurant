package com.c1ph3r.zomatorestaurant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c1ph3r.zomatorestaurant.Adapter.RegistrationProcess;
import com.c1ph3r.zomatorestaurant.Controller.AuthController;
import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegisterPage1Binding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegistrationPage2Binding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class RegisterRestaurant extends AppCompatActivity {
    String url;
    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;
    private FragmentRegisterPage1Binding PAGE1;
    FragmentRegistrationPage2Binding PAGE2;
    RegisterPage1 restaurantDetails;
    RestaurantUserDetails userDetails;
    RegistrationPage2 documentDetails;
    View view ;
    String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding view.
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobileNumber");
        if(mobileNumber!= null)
            restaurantDetails = new RegisterPage1(mobileNumber);
        else
            restaurantDetails = new RegisterPage1();

        // Initializing and Declaring the fragments.
        documentDetails = new RegistrationPage2();





        // Initializing and Declaring the Adapter.
        RegistrationProcess process = new RegistrationProcess(RegisterRestaurant.this);
        // Adding the fragments to the Adapter.
        process.addFragment(restaurantDetails);
        process.addFragment(documentDetails);

        // Setting Adapter to the View Pager2.
        NEW_RESTAURANT.RegistrationProcess.setAdapter(process);
        NEW_RESTAURANT.RegistrationProcess.setUserInputEnabled(false);


        // Binding Fragments using variables.


        // On Click of Next Button verify the values entered by the user and change the fragments
        // At the end verify the user data and save all the data to the db.
        NEW_RESTAURANT.NextButton.setOnClickListener(OnClickNext -> {
            // Finding the current fragment in the view pager.
            switch (NEW_RESTAURANT.RegistrationProcess.getCurrentItem()) {
                case 0:
                    // If view pager in 1st fragment verify the inputs and change to next fragments.
                    if (checkInputs())
                        nextPage();
                    break;
                case 1:
                    // If view pager in 2nd fragment verify if the user uploaded the document and if yes move to next fragments
                    if (CheckDocumentValues())
                        submitValues();
                    //else
                        // Else toast a message to the user.
                        //alertTheUser(getString(R.string.ImageError_Text), getString(R.string.DocumentErrorMessage));
            }
        });


    }

    @SuppressLint("ObjectAnimatorBinding")
    private void submitValues() {
        PAGE2.loadingProgress.setVisibility(View.VISIBLE);
        uploadImage();
    }

    private void uploadDataToDB() {
        FirebaseFirestore restaurantDB = FirebaseFirestore.getInstance();
        userDetails.setMobileNumber((getString(R.string.CountryCode) + PAGE1.ContactNumber.getText()));
        DocumentReference restaurantMerchant = restaurantDB.collection(getString(R.string.RESTAURANT_DB)).document(userDetails.getMobileNumber());
        restaurantMerchant.set(userDetails).addOnSuccessListener(success -> PAGE2.loadingProgress.setVisibility(View.INVISIBLE))
                .addOnFailureListener(failed -> System.out.println(getString(R.string.Failed_Text)));
    }

    private void uploadImage() {
        StorageReference documentsDB = FirebaseStorage.getInstance().getReference("restaurantDocuments/")
                .child(UUID.randomUUID().toString());
        UploadTask uploadImage = documentsDB.putFile(documentDetails.imageUri);
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
                    PAGE2.loadingProgress.setVisibility(View.INVISIBLE);
                }).continueWithTask(
                        task -> documentsDB.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    Uri uri = task.getResult();
                    url = uri.toString();
                    System.out.println(url);
                    userDetails.setFSSAICertificate(url);
                    uploadDataToDB();
                });

    }

    private boolean CheckDocumentValues() {
        View view2 = documentDetails.getView();
        assert view2 != null;
        PAGE2 = FragmentRegistrationPage2Binding.bind(view2);
        return PAGE2.PreviewDocument.getDrawable() != null;
    }

    private void nextPage() {
        NEW_RESTAURANT.RegistrationProcess.setCurrentItem(1);
        NEW_RESTAURANT.NextButton.setText(R.string.submit_Text);
    }

    private boolean checkInputs() {
        view = restaurantDetails.getView();
        assert view != null;
        PAGE1 = FragmentRegisterPage1Binding.bind(view);
        userDetails = new RestaurantUserDetails();
        userDetails.setTypeOfFoodServed(new ArrayList<String>());
        userDetails.setLocation(new GeoPoint(0,0));
        userDetails.setAddress(" ");
        userDetails.setRestaurantStatus(false);
        userDetails.setTopFoodImages(new ArrayList<String>());
        userDetails.setMobileNumber((Objects.requireNonNull(PAGE1.ContactNumber.getText())).toString());
        userDetails.setGSTNumber((PAGE1.GSTNo.getText() == null)?"":PAGE1.GSTNo.getText().toString());
        userDetails.setPanCardNumber(Objects.requireNonNull(PAGE1.PanCardNumber.getText()).toString());
        userDetails.setRestaurantName(Objects.requireNonNull(PAGE1.RestaurantName.getText()).toString());
        userDetails.setFSSAINumber(Objects.requireNonNull(PAGE1.FSSAINumber.getText()).toString());
        userDetails.setOwnerName(Objects.requireNonNull(PAGE1.OwnerName.getText()).toString());

        Dialog dialog = new Dialog(this);
        AuthController Auth = new AuthController();
        return Auth.checkValues(userDetails, dialog);
    }


    @Override
    public void onBackPressed() {
        if(NEW_RESTAURANT.RegistrationProcess.getCurrentItem() == 0){
            if(FirebaseAuth.getInstance().getCurrentUser() != null){
                new MaterialAlertDialogBuilder(this, R.style.customDialogBoxBackground)
                        .setTitle("Zomato Restaurant")
                        .setMessage("Do you want to Logout?")
                        .setPositiveButton("Yes", (dialogInterface, i) ->{
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(RegisterRestaurant.this, MainActivity.class));
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> {})
                        .show();
            }
        }
        else{
            new MaterialAlertDialogBuilder(this, R.style.customDialogBoxBackground)
                    .setTitle("Zomato Restaurant")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", (dialogInterface, i) ->{
                        startActivity(new Intent(RegisterRestaurant.this, MainActivity.class));
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> {})
                    .show();
        }
    }
}