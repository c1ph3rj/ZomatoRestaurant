package com.c1ph3r.zomatorestaurant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class OTPVerify extends Fragment {


    String mobileNumber;
    FirebaseAuth auth;
    PhoneAuthCredential phoneAuthCredential;
    String verificationID;
    RestaurantUserDetails restaurantDB;

    public OTPVerify() {
        // Required empty public constructor
    }

    public OTPVerify(String mobileNumber, FirebaseAuth auth, String verificationID) {
        this.mobileNumber = mobileNumber;
        this.auth = auth;
        this.verificationID = verificationID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_o_t_p_verify, container, false);

        if(view != null){
// Initializing the required variables.
            MaterialButton verifyOTP = view.findViewById(R.id.verifyButton);
            EditText OTP = view.findViewById(R.id.OTPField);
            MaterialButton back = view.findViewById(R.id.backToLogin);
            TextView mobileNumberDisplay = view.findViewById(R.id.mobileNumberDisplay);
            this.mobileNumber = getString(R.string.CountryCode) + mobileNumber;
            mobileNumberDisplay.setText(mobileNumber);



            // Verifying the OTP using firebase auth.
            verifyOTP.setOnClickListener(OnClickVerify -> {
                if (OTP.getText().length() == 6) {
                    // Getting the credentials of the user to verify the user.
                    phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, OTP.getText().toString());
                    // Method to verify the user.
                    signInTheUser();
                } else
                    // if the password is wrong alter the user.
                    Toast.makeText(requireActivity(),"Enter a valid OTP.", Toast.LENGTH_SHORT).show();
            });

            // On Click of back button.
            back.setOnClickListener(OnClickBack -> requireActivity().getSupportFragmentManager().popBackStack());
        }

        // Returning the view.
        return view;
    }


    // Method to signInTheUser in firebase auth.
    private void signInTheUser() {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // If successfully signed in change the activity.
                ifTheUserIsNew();
                // alter the user about the login status.
                //Toast.makeText(requireActivity(), R.string.LoginStatus1, Toast.LENGTH_SHORT).show();
            } else
                // alter the user if the login failed.
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void ifTheUserIsNew() {
        FirebaseFirestore FireStore = FirebaseFirestore.getInstance();
        DocumentReference RestaurantDB = FireStore.collection(getString(R.string.RESTAURANT_DB)).document(mobileNumber);
        RestaurantDB.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot restaurantDetails) {
                if(restaurantDetails.exists() && restaurantDetails.get("restaurantName") != "")
                    startActivity(new Intent(requireActivity(), Dashboard.class));
                else if(restaurantDetails.exists() && ((ArrayList<?>) Objects.requireNonNull(restaurantDetails.get("topFoodImages"))).size() !=0)
                    startActivity(new Intent(requireActivity(), RestaurantDetails.class));
                else {
                    Intent intent = new Intent(requireActivity(), RegisterRestaurant.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    startActivity(intent);
                }
            }
        });
    }
}