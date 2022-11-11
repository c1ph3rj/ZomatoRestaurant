package com.c1ph3r.zomatorestaurant.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RestaurantUserDetails {
    private String mobileNumber;
    private String ownerName;
    private String panCardNumber;
    private String restaurantName;
    private String restaurantStatus;
    private ArrayList<String> typeOfFoodServed;
    private String FSSAINumber;
    private String GSTNumber;
    private String address;
    private GeoPoint location;
    private String FSSAICertificate;
}
