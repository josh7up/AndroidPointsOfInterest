package com.example.josh.pointsofinterest;

import com.example.josh.pointsofinterest.rest.nearby.NearbySearchResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NearbyPlacesRestService {

    @GET("maps/api/place/nearbysearch/json")
    Call<NearbySearchResults> getNearbyPlaces(@Query("key") String apiKey, @Query("location") String commaSeparatedLatLon, @Query("radius") long radius);
}
