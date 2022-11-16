package com.c1ph3r.zomatorestaurant;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.c1ph3r.zomatorestaurant.Adapter.ListOfImagesViewerAdapter;
import com.c1ph3r.zomatorestaurant.Controller.dialogBox;
import com.c1ph3r.zomatorestaurant.Model.RestaurantUserDetails;
import com.c1ph3r.zomatorestaurant.databinding.ActivityRestaurantDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class RestaurantDetails extends AppCompatActivity {

    private ActivityRestaurantDetailsBinding RESTAURANT_DETAILS;
    String mobileNumber;
    RestaurantUserDetails RestaurantDB;
    ActivityResultLauncher<Intent> getTopFoodImages;
    ArrayList<Uri> listOfImages;
    ArrayList<Uri> topFoodImages;
    ListOfImagesViewerAdapter listOfImagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAURANT_DETAILS = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        View view = RESTAURANT_DETAILS.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        mobileNumber = intent.getStringExtra("mobileNumber");

        setRestaurantName();
        getTopFoodImages();



        RESTAURANT_DETAILS.UploadTopFoodImages.setOnClickListener(onClickToGetImages -> {
            Intent intent1 = new Intent();
            intent1.setType("image/*");
            intent1.setAction(Intent.ACTION_GET_CONTENT);
            intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            getTopFoodImages.launch(intent1);
        });


        RESTAURANT_DETAILS.UploadImagesToDB.setOnClickListener(OnClickToUploadImages ->{
            ProgressDialog progressDialog = new ProgressDialog(RestaurantDetails.this);
            progressDialog.setTitle("uploading Please Be patient.");
            progressDialog.setCancelable(false);
            progressDialog.show();
            uploadDataToDB(progressDialog);
        });



    }

    private void getTopFoodImages() {

        getTopFoodImages = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    listOfImages = new ArrayList<>();
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        if (result.getData().getClipData() != null) {
                             int iterate = Math.min(result.getData().getClipData().getItemCount(), 5);
                            System.out.println(iterate);
                            if (iterate == 5)
                                dialogBox.alertTheUser("Maximum upload limit reached!", "You cannot upload more than 5 images, Select the best 5 out of all images.", this);
                            for (int i = 0; i < iterate; i++) {
                                listOfImages.add(result.getData().getClipData().getItemAt(i).getUri());
                            }
                        }else if(result.getData() != null)
                            listOfImages.add(result.getData().getData());
                        listOfImagesAdapter = new ListOfImagesViewerAdapter(RestaurantDetails.this, listOfImages);
                       RESTAURANT_DETAILS.listOfImagesViewer.setAdapter(listOfImagesAdapter);
                       RESTAURANT_DETAILS.listOfImagesViewer.setLayoutManager(new LinearLayoutManager(RestaurantDetails.this));
                    }

                }
        );
    }



    private void uploadDataToDB(ProgressDialog progressDialog) {
        topFoodImages = new ArrayList<>();
        FirebaseStorage FireStorage = FirebaseStorage.getInstance();
        for(Uri Image : listOfImages){
            StorageReference TopFoodImages = FireStorage.getReference("restaurantDocuments/" + mobileNumber + "/topFoodImages").child(UUID.randomUUID().toString());
            UploadTask uploadTask =TopFoodImages.putFile(Image);
            uploadTask.addOnCompleteListener(task -> TopFoodImages.getDownloadUrl()
                    .addOnCompleteListener(linkGenerator -> {
                        topFoodImages.add(linkGenerator.getResult());
                        updateToDB(uploadTask, Image, progressDialog);
                    }))
                    .addOnFailureListener(Throwable::printStackTrace);

        }



    }

    private void updateToDB(UploadTask uploadTask, Uri Image, ProgressDialog progressDialog) {
        if(listOfImages.indexOf(Image) == listOfImages.size()-1){
            uploadTask.addOnCompleteListener(uploadToFireStore -> {
                System.out.println(topFoodImages.toString());
                System.out.println("HELLO THERE " + "\n\n\n\n\n");
                FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
                DocumentReference RestaurantDB = fireStore.collection(getString(R.string.RESTAURANT_DB)).document(mobileNumber);
                RestaurantDB.update("topFoodImages", topFoodImages)
                        .addOnCompleteListener(OnSuccess ->{
                            progressDialog.dismiss();
                            startActivity(new Intent(RestaurantDetails.this, Dashboard.class));
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            dialogBox.alertTheUser("Failed", "Failed to upload the images to the DB! Check your internet connection and try again.",RestaurantDetails.this);
                        });
            });
        }
    }

    private void setRestaurantName() {
        FirebaseFirestore FireStore = FirebaseFirestore.getInstance();
        DocumentReference DataBase = FireStore.collection(getString(R.string.RESTAURANT_DB)).document(mobileNumber);
        DataBase.get().addOnSuccessListener(documentSnapshot -> {
            RestaurantDB = documentSnapshot.toObject(RestaurantUserDetails.class);
            if(RestaurantDB != null){
                RESTAURANT_DETAILS.RestaurantName.setText(RestaurantDB.getRestaurantName());
            }
        });
    }
}