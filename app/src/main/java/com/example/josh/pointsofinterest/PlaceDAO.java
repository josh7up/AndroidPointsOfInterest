package com.example.josh.pointsofinterest;

import android.content.Context;
import android.util.Log;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

public class PlaceDAO {

    private Context mContext;

    public PlaceDAO(Context context) {
        mContext = context;
    }

    public void savePlaces(double lat, double lon, List<PlaceModel> placeModels) {
        DB db = null;
        try {
            db = DBFactory.open(mContext);
            db.put(getKey(lat, lon), placeModels);
        } catch (SnappydbException e) {
            Log.e(getClass().getName(), "Error in savePlaces", e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e) {
                }
            }
        }
    }

    public boolean hasPlaces(double lat, double lon) {
        DB db = null;
        try {
            db = DBFactory.open(mContext);
            boolean keyExists = db.exists(getKey(lat, lon));
            Log.d(getClass().getName(), "Has places for key " + getKey(lat, lon) + "? " + keyExists);
            return keyExists;
        } catch (SnappydbException e) {
            Log.e(getClass().getName(), "Error in hasPlaces", e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e) {
                }
            }
        }
        return false;
    }

    public List<PlaceModel> getPlaces(double lat, double lon) {
        DB db = null;
        try {
            db = DBFactory.open(mContext);
            return (List<PlaceModel>) db.getObject(getKey(lat, lon), ArrayList.class);
        } catch (SnappydbException e) {
            Log.e(getClass().getName(), "Error in getPlaces", e);
        } finally {
            if (db != null) {
                try {
                    db.close();
                } catch (SnappydbException e) {
                }
            }
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
}
