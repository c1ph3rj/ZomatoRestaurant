package com.c1ph3r.zomatorestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.c1ph3r.zomatorestaurant.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding MAIN;
    private FirebaseAuth auth;
    private String verificationID;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MAIN = ActivityMainBinding.inflate(getLayoutInflater());
        View view = MAIN.getRoot();
        setContentView(view);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());

        auth = FirebaseAuth.getInstance();

        MAIN.LoginUser.setOnClickListener(onClickLogin -> {
            PhoneAuthOptions sendOTPAuth = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber("+91 "+ MAIN.MobileNumber.getText())
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(MainActivity.this)
                    .setCallbacks(responseCallBacks).build();
            PhoneAuthProvider.verifyPhoneNumber(sendOTPAuth);
        });

    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks responseCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            System.out.println("Failed");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
            System.out.println("DONE SMS SENT");
            verifyOTP(verificationID);
        }
    };

    private void verifyOTP(String verificationID) {

    }
}