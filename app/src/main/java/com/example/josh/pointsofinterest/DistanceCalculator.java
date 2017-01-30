package com.example.josh.pointsofinterest;

import android.location.Location;

public class DistanceCalculator {

    private static final float METERS_IN_MILE = 1609.34f;

    public float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float [] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0] / METERS_IN_MILE;
    }
}
