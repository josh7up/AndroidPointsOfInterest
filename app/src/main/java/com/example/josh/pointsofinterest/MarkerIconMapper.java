package com.example.josh.pointsofinterest;

import com.google.android.gms.location.places.Place;

import java.util.HashMap;

public class MarkerIconMapper {

    public static final int DEFAULT_MARKER_ID = -1;
    private static final HashMap<Integer, Integer> typeIdToResourceIdMap = new HashMap<>();

    static {
        typeIdToResourceIdMap.put(Place.TYPE_ACCOUNTING, R.drawable.accountancy);
        typeIdToResourceIdMap.put(Place.TYPE_HEALTH, R.drawable.health_medical);
        typeIdToResourceIdMap.put(Place.TYPE_CAR_REPAIR, R.drawable.automotive);
        typeIdToResourceIdMap.put(Place.TYPE_BAR, R.drawable.bars);
        typeIdToResourceIdMap.put(Place.TYPE_DENTIST, R.drawable.dental);
        typeIdToResourceIdMap.put(Place.TYPE_ELECTRONICS_STORE, R.drawable.electronics);
        typeIdToResourceIdMap.put(Place.TYPE_DOCTOR, R.drawable.doctors);
        typeIdToResourceIdMap.put(Place.TYPE_MOVIE_THEATER, R.drawable.movies);
        typeIdToResourceIdMap.put(Place.TYPE_FOOD, R.drawable.food);
        typeIdToResourceIdMap.put(Place.TYPE_FURNITURE_STORE, R.drawable.furniture_stores);
        typeIdToResourceIdMap.put(Place.TYPE_SCHOOL, R.drawable.schools);
        typeIdToResourceIdMap.put(Place.TYPE_STADIUM, R.drawable.sports);
        typeIdToResourceIdMap.put(Place.TYPE_CAFE, R.drawable.coffee_n_tea);
        typeIdToResourceIdMap.put(Place.TYPE_RESTAURANT, R.drawable.restaurants);
        typeIdToResourceIdMap.put(Place.TYPE_SHOPPING_MALL, R.drawable.shopping);
        typeIdToResourceIdMap.put(Place.TYPE_PET_STORE, R.drawable.pets);
        typeIdToResourceIdMap.put(Place.TYPE_STREET_ADDRESS, R.drawable.residential_places);
        typeIdToResourceIdMap.put(Place.TYPE_BUS_STATION, R.drawable.transport);
        typeIdToResourceIdMap.put(Place.TYPE_TRANSIT_STATION, R.drawable.transport);
    }

    public static int getMarkerResourceId(int placeTypeId) {
        Integer resourceId = typeIdToResourceIdMap.get(placeTypeId);
        return resourceId != null ? resourceId : DEFAULT_MARKER_ID;
    }
}
