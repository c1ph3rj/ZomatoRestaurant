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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(String panCardNumber) {
        this.panCardNumber = panCardNumber;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantStatus() {
        return restaurantStatus;
    }

    public void setRestaurantStatus(String restaurantStatus) {
        this.restaurantStatus = restaurantStatus;
    }

    public ArrayList<String> getTypeOfFoodServed() {
        return typeOfFoodServed;
    }

    public void setTypeOfFoodServed(ArrayList<String> typeOfFoodServed) {
        this.typeOfFoodServed = typeOfFoodServed;
    }

    public String getFSSAINumber() {
        return FSSAINumber;
    }

    public void setFSSAINumber(String FSSAINumber) {
        this.FSSAINumber = FSSAINumber;
    }

    public String getGSTNumber() {
        return GSTNumber;
    }

    public void setGSTNumber(String GSTNumber) {
        this.GSTNumber = GSTNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getFSSAICertificate() {
        return FSSAICertificate;
    }

    public void setFSSAICertificate(String FSSAICertificate) {
        this.FSSAICertificate = FSSAICertificate;
    }

    public RestaurantUserDetails(String mobileNumber, String ownerName, String panCardNumber, String restaurantName, String restaurantStatus, ArrayList<String> typeOfFoodServed, String FSSAINumber, String GSTNumber, String address, GeoPoint location, String FSSAICertificate) {
        this.mobileNumber = mobileNumber;
        this.ownerName = ownerName;
        this.panCardNumber = panCardNumber;
        this.restaurantName = restaurantName;
        this.restaurantStatus = restaurantStatus;
        this.typeOfFoodServed = typeOfFoodServed;
        this.FSSAINumber = FSSAINumber;
        this.GSTNumber = GSTNumber;
        this.address = address;
        this.location = location;
        this.FSSAICertificate = FSSAICertificate;
    }

    public RestaurantUserDetails(){

    }
}
