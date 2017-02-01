package com.example.josh.pointsofinterest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PlaceCache {

    private static final Gson gson = new Gson();

    private SharedPreferences mSharedPreferences;

    public PlaceCache(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean hasPlaces(double lat, double lon) {
        boolean hasKey = mSharedPreferences.contains(getKey(lat, lon));
        Log.d(getClass().getName(), "Has places for key " + getKey(lat, lon) + "? " + hasKey);
        return hasKey;
    }

    public List<PlaceModel> getPlaces(double lat, double lon) {
        String json = mSharedPreferences.getString(getKey(lat, lon), null);
        if (json != null) {
            return gson.fromJson(json, new TypeToken<ArrayList<PlaceModel>>() {}.getType());
        }
        return new ArrayList<>();
    }

    private String getKey(double lat, double lon) {
        double roundedLat = round(lat);
        double roundedLon = round(lon);
        return String.format("%s,%s", roundedLat, roundedLon);
    }

    /**
     * Rounds a given coordinate to the nearest hundredth place.
     */
    private double round(double coordinate) {
        return Math.round(coordinate * 100) / 100d;
    }

    public void savePlaces(double lat, double lon, List<PlaceModel> placeModels) {
        mSharedPreferences.edit().putString(getKey(lat, lon), gson.toJson(placeModels)).apply();
    }
}
