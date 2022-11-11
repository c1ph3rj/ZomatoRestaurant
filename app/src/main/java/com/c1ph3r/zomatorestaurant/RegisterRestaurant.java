package com.c1ph3r.zomatorestaurant;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c1ph3r.zomatorestaurant.Adapter.RegistrationProcess;
import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRegisterRestaurantBinding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegisterPage1Binding;
import com.c1ph3r.zomatorestaurant.databinding.FragmentRegistrationPage2Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class RegisterRestaurant extends AppCompatActivity {
    String url;
    private ActivityRegisterRestaurantBinding NEW_RESTAURANT;
    private FragmentRegisterPage1Binding PAGE1;
    FragmentRegistrationPage2Binding PAGE2;
    RegisterPage1 restaurantDetails;
    RegistrationPage2 documentDetails;
    View view ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Binding view.
        NEW_RESTAURANT = ActivityRegisterRestaurantBinding.inflate(getLayoutInflater());
        View view = NEW_RESTAURANT.getRoot();
        setContentView(view);
        // Initializing and Declaring the fragments.
        restaurantDetails = new RegisterPage1();
        documentDetails = new RegistrationPage2();

        view = restaurantDetails.getView();

        // Initializing and Declaring the Adapter.
        RegistrationProcess process = new RegistrationProcess(RegisterRestaurant.this);
        // Adding the fragments to the Adapter.
        process.addFragment(restaurantDetails);
        process.addFragment(documentDetails);

        // Setting Adapter to the View Pager2.
        NEW_RESTAURANT.RegistrationProcess.setAdapter(process);
        NEW_RESTAURANT.RegistrationProcess.setUserInputEnabled(false);

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
                    else
                        // Else toast a message to the user.
                        alertTheUser(getString(R.string.ImageError_Text), getString(R.string.DocumentErrorMessage));
            }
        });


    }

    private void submitValues() {

        uploadImage();


    }

    private void uploadDataToDB(DocumentReference restaurantMerchant) {
        RestaurantUserDetails userDetails = new RestaurantUserDetails();
        userDetails.setMobileNumber(Objects.requireNonNull(PAGE1.ContactNumber.getText()).toString());
        userDetails.setFSSAICertificate(url);
        userDetails.setPanCardNumber(Objects.requireNonNull(PAGE1.PanCardNumber.getText()).toString());
        userDetails.setRestaurantName(Objects.requireNonNull(PAGE1.RestaurantName.getText()).toString());
        userDetails.setFSSAINumber(Objects.requireNonNull(PAGE1.FSSAINumber.getText()).toString());
        userDetails.setOwnerName(Objects.requireNonNull(PAGE1.OwnerName.getText()).toString());
        restaurantMerchant.set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterRestaurant.this, "Saved to DB", Toast.LENGTH_SHORT).show();
            }
        });
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
                }).continueWithTask(
                        task -> documentsDB.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    Uri uri = task.getResult();
                    url = uri.toString();

                    System.out.println(url);
                    FirebaseFirestore restaurantDB = FirebaseFirestore.getInstance();
                    DocumentReference restaurantMerchant = restaurantDB.collection("restaurantDataBase").document(PAGE1.ContactNumber.getText().toString());
                    uploadDataToDB(restaurantMerchant);
                });
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
        if (view != null) {
            PAGE1 = FragmentRegisterPage1Binding.bind(view);
            if (!PAGE1.OwnerName.getText().toString().matches("^[A-Za-z]\\w{5,28}$")) {
                alertTheUser("Enter a valid UserName!", "UserName must starts with alphabets(Aa-Zz) and minimum 7 not more than 28 words, and Do not contain Space.");
                return false;
            }

            if (PAGE1.RestaurantName.getText().toString().isEmpty()) {
                alertTheUser("Enter a valid Restaurant Name!", "Restaurant Name cannot be empty.");
                return false;
            }

            if (!PAGE1.GSTNo.getText().toString().isEmpty()) {
                if (!PAGE1.GSTNo.getText().toString().matches("^[1-9][0-9]{14}$")) {
                    alertTheUser("Enter a valid GST Number!", "GST Number you have entered is not valid");
                    return false;
                }
            }
            if (!PAGE1.FSSAINumber.getText().toString().matches("^[A-Za-z1-9][A-Za-z0-9]{14}$")) {
                alertTheUser("Enter a valid FSSAINumber!", "FSSAINumber you have entered is not valid");
                return false;
            }

            if (!PAGE1.PanCardNumber.getText().toString().matches("^[A-Za-z1-9][A-Za-z0-9]{9}$")) {
                alertTheUser("Enter a valid PanCard Number!", "PanCard Number you have entered is not valid");
                return false;
            }

            if (!PAGE1.ContactNumber.getText().toString().matches("^[6-9][0-9]{9}$")) {
                alertTheUser("Enter a valid Contact Number!", "Contact Number you have entered is not valid");
                return false;
            }

            return true;
        } else
            return false;
    }


    void alertTheUser(String Title, String Message) {
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

        dialog.show();

    }
}