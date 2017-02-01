package com.example.josh.pointsofinterest;

import android.content.Context;

import com.example.josh.pointsofinterest.rest.nearby.NearbySearchResults;
import com.google.android.gms.location.places.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceService {

    private static final long SEARCH_RADIUS_METERS = 1000;
    private static final String BASE_URL = "https://maps.googleapis.com/";

    private Context mContext;
    private NearbyPlacesRestService mNearbyPlacesRestService;

    public PlaceService(Context context) {
        mContext = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNearbyPlacesRestService = retrofit.create(NearbyPlacesRestService.class);
    }

    public interface OnNearbyPlacesCallback {
        void onSuccess(List<PlaceModel> placeModels);
        void onFailure(Throwable t);
    }

    public void getNearbyPlaces(double lat, double lon, final OnNearbyPlacesCallback onNearbyPlacesCallback) {
        if (onNearbyPlacesCallback == null) {
            throw new IllegalArgumentException("onNearbyPlacesCallback cannot be null.");
        }

        Call<NearbySearchResults> nearbySearchResultsCall = mNearbyPlacesRestService.getNearbyPlaces(
            mContext.getString(R.string.googleMapsApiKey),
            String.format("%s,%s", lat, lon),
            SEARCH_RADIUS_METERS
        );

        nearbySearchResultsCall.enqueue(new Callback<NearbySearchResults>() {
            @Override
            public void onResponse(Call<NearbySearchResults> call, Response<NearbySearchResults> response) {
                NearbySearchResults nearbySearchResults = response.body();
                NearbySearchConverter nearbySearchConverter = new NearbySearchConverter();
                List<PlaceModel> placeModels = nearbySearchConverter.getPlaceModels(nearbySearchResults);
                onNearbyPlacesCallback.onSuccess(placeModels);
            }

            @Override
            public void onFailure(Call<NearbySearchResults> call, Throwable t) {
                onNearbyPlacesCallback.onFailure(t);
            }
        });
    }
}
