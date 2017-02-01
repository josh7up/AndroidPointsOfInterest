package com.example.josh.pointsofinterest;

import com.google.android.gms.location.places.Place;

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
}
