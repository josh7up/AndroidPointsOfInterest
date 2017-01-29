package com.example.josh.pointsofinterest;

import android.net.Uri;

import com.google.android.gms.location.places.PlaceLikelihood;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceModel implements Serializable {

    private String description;
    private String address;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String websiteUrl;
    private double lat, lon;
    private String name;
    private List<Integer> placeTypes;

    public PlaceModel(PlaceLikelihood placeLikelihood) {
        this.description = (String) placeLikelihood.getPlace().getAttributions();
        this.address = (String) placeLikelihood.getPlace().getAddress();
        this.phoneNumber = placeLikelihood.getPlace().getPhoneNumber().toString();
        this.rating = placeLikelihood.getPlace().getRating();
        this.priceLevel = placeLikelihood.getPlace().getPriceLevel();
        this.websiteUrl = placeLikelihood.getPlace().getWebsiteUri() != null ? placeLikelihood.getPlace().getWebsiteUri().toString() : null;
        this.lat = placeLikelihood.getPlace().getLatLng().latitude;
        this.lon = placeLikelihood.getPlace().getLatLng().longitude;
        this.name = (String) placeLikelihood.getPlace().getName();
        this.placeTypes = placeLikelihood.getPlace().getPlaceTypes() != null ? placeLikelihood.getPlace().getPlaceTypes() : new ArrayList<Integer>();
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public float getRating() {
        return rating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }
}
