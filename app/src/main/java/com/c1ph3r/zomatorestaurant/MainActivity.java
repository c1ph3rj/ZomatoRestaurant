package com.c1ph3r.zomatorestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.util.Objects;
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

        MAIN.loginUser.setOnClickListener(onClickLogin -> {
            if(MAIN.MobileNumber.getText().toString().matches("^[6-9][0-9]{9}$")) {
                toSendOTP();
                startCountDown();
            }else
                Toast.makeText(this, "Enter a valid Mobile Number!", Toast.LENGTH_LONG).show();
        });



    }

    private CountDownTimer startCountDown() {
        return new CountDownTimer(60000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                MAIN.TimeoutTimer.setText( "Wait Until: " + millisUntilFinished / 1000);

            }

            public void onFinish() {
                MAIN.loginUser.setClickable(true);
                MAIN.TimeoutTimer.setText(" ");
            }
        }.start();
    }

    private void toSendOTP() {
        MAIN.loginUser.setClickable(false);
        PhoneAuthOptions sendOTPAuth = PhoneAuthOptions.newBuilder()
                .setPhoneNumber("+91 "+ MAIN.MobileNumber.getText())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(MainActivity.this)
                .setCallbacks(responseCallBacks).build();
        PhoneAuthProvider.verifyPhoneNumber(sendOTPAuth);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks responseCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
            Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            e.printStackTrace();
            System.out.println("Failed");
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
            System.out.println("DONE SMS SENT");
            verifyOTP(verificationID);
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };

    private void verifyOTP(String verificationID) {
        try {
            OTPVerify fragment = new OTPVerify(Objects.requireNonNull(MAIN.MobileNumber.getText()).toString(), auth, verificationID);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.LoginScreen, fragment).addToBackStack("BackPressed");
            fragmentTransaction.commit();
            MAIN.loginUser.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MAIN.loginUser.setVisibility(View.VISIBLE);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
    }
}