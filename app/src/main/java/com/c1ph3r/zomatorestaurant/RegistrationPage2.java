package com.c1ph3r.zomatorestaurant;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.button.MaterialButton;

public class RegistrationPage2 extends Fragment {
    ImageView previewDocument;
    ActivityResultLauncher<Intent> openGallery;

    public RegistrationPage2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_page2, container, false);

        if(view != null){
            MaterialButton chooseFile = view.findViewById(R.id.uploadDocument);
            chooseFile.setOnClickListener(onClickChooseFile -> {
                openGallery();
            });
            previewDocument = view.findViewById(R.id.PreviewDocument);
            openSomeActivityForResult();

        }

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        openGallery.launch(Intent.createChooser(intent,"Select Document"));
    }

    public void openSomeActivityForResult(){
        openGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        assert data != null;
                        Uri imageUri = data.getData();
                        previewDocument.setImageURI(imageUri);
                    }
                });
    }
}