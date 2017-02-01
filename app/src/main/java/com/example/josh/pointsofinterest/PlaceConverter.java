package com.example.josh.pointsofinterest;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;

import java.util.ArrayList;
import java.util.List;

public class PlaceConverter {

    public PlaceModel convert(Place place) {
        PlaceModel.Builder placeBuilder = new PlaceModel.Builder()
            .address((String) place.getAddress())
            .placeTypes(place.getPlaceTypes())
            .description((String) place.getAttributions())
            .phoneNumber((String) place.getPhoneNumber())
            .latLon(place.getLatLng().latitude, place.getLatLng().longitude)
            .name((String) place.getName())
            .priceLevel(place.getPriceLevel())
            .rating(place.getRating())
            .id(place.getId());

        if (place.getWebsiteUri() != null) {
            placeBuilder.websiteUrl(place.getWebsiteUri().toString());
        }
        return placeBuilder.build();
    }

    public List<PlaceModel> convert(PlaceLikelihoodBuffer likelyPlaces) {
        List<PlaceModel> placeModels = new ArrayList<>();
        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            placeModels.add(convert(placeLikelihood));
        }
        return placeModels;
    }

    public PlaceModel convert(PlaceLikelihood placeLikelihood) {
        Place place = placeLikelihood.getPlace();
        return new PlaceModel.Builder()
            .id(place.getId())
            .description((String) place.getAttributions())
            .address((String) place.getAddress())
            .phoneNumber(place.getPhoneNumber().toString())
            .rating(place.getRating())
            .priceLevel(place.getPriceLevel())
            .websiteUrl(place.getWebsiteUri() != null ? placeLikelihood.getPlace().getWebsiteUri().toString() : null)
            .latLon(place.getLatLng().latitude, placeLikelihood.getPlace().getLatLng().longitude)
            .name((String) place.getName())
            .placeTypes(place.getPlaceTypes() != null ? place.getPlaceTypes() : new ArrayList<Integer>())
            .build();
    }
}
