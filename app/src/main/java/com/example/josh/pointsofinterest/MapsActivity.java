package com.example.josh.pointsofinterest;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnPlaceSelectedListener {

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 1000 * 60 * 5;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float POINTS_OF_INTEREST_ZOOM_LEVEL = 16;
    private static final String PLACE_MODELS_KEY = "places";

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Marker mMyLocationMarker;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private final Gson gson = new Gson();
    private List<PlaceModel> mPlaceModels = new ArrayList<>();
    private List<Marker> mMarkers = new ArrayList<>();
    private PlaceModel mMyLocationPlaceModel;
    private DistanceCalculator mDistanceCalculator = new DistanceCalculator();
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesRecyclerViewAdapter mPlacesAdapter;

    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.placesView) RecyclerView mPlacesRecyclerView;
    @BindView(R.id.placesContainer) View mPlacesContainer;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open_description, R.string.drawer_close_description);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlacesRecyclerView.setLayoutManager(mLinearLayoutManager);

        mToolbar.setNavigationIcon(R.drawable.hamburger);
        mToolbar.setTitle(getString(R.string.toolbar_title));
        setSupportActionBar(mToolbar);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mPlacesAdapter != null) {
                    PlaceModel placeModel = (PlaceModel) marker.getTag();
                    int adapterPosition = mPlacesAdapter.getIndex(placeModel);
                    if (adapterPosition >= 0) {
                        mPlacesRecyclerView.smoothScrollToPosition(adapterPosition);
                        mPlacesAdapter.setSelectedPlace(placeModel);
                    }
                }
                return false;
            }
        });
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The location permission was authorized by the user.
                    if (mGoogleApiClient.isConnected()) {
                        LatLng latLng = getLastKnownCurrentLocation();
                        if (latLng != null) {
                            onMyLocationFound(latLng);
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    }
                }
                break;
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void displayPointsOfInterest() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        /*
         * Cache the nearby places for development purposes. This will reduce the number of Google API calls and
         * will allow the markers to load more quickly.
         */
        if (sharedPreferences.contains(PLACE_MODELS_KEY)) {
            String placesJson = sharedPreferences.getString(PLACE_MODELS_KEY, null);
            List<PlaceModel> placeModels = gson.fromJson(placesJson, new TypeToken<ArrayList<PlaceModel>>() {}.getType());
            updateViews(placeModels);
        } else {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    List<PlaceModel> placeModels = toPlaceModels(likelyPlaces);
                    likelyPlaces.release();
                    updateViews(placeModels);
                    // Persist the models for development purposes.
                    sharedPreferences.edit().putString(PLACE_MODELS_KEY, gson.toJson(mPlaceModels)).apply();
                }
            });
        }
    }

    private void updateViews(List <PlaceModel> placeModels) {
        clearMapMarkers();
        mPlaceModels = updateDistances(placeModels);
        mMarkers = addMarkers(placeModels);

        mPlacesAdapter = new PlacesRecyclerViewAdapter(mPlaceModels, this, this);
        mPlacesRecyclerView.setAdapter(mPlacesAdapter);
    }

    private List<PlaceModel> updateDistances(List<PlaceModel> placeModels) {
        for (PlaceModel placeModel : placeModels) {
            // Calculate the distance from the current location.
            if (mMyLocationPlaceModel != null) {
                float distance = mDistanceCalculator.calculateDistance(
                        mMyLocationPlaceModel.getLat(),
                        mMyLocationPlaceModel.getLon(),
                        placeModel.getLat(),
                        placeModel.getLon());
                placeModel.setDistance(distance);
            }
        }
        return placeModels;
    }

    private List<Marker> addMarkers(List<PlaceModel> placeModels) {
        List<Marker> markers = new ArrayList<>();
        for (PlaceModel placeModel : placeModels) {
            Marker marker = mMap.addMarker(getMarkerOptions(placeModel));
            marker.setTag(placeModel);
            markers.add(marker);
        }
        return markers;
    }

    private List<PlaceModel> toPlaceModels(PlaceLikelihoodBuffer likelyPlaces) {
        List<PlaceModel> placeModels = new ArrayList<>();
        for (PlaceLikelihood placeLikelihood : likelyPlaces) {
            placeModels.add(new PlaceModel(placeLikelihood));
        }
        return placeModels;
    }

    private void clearMapMarkers() {
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();
    }

    @NonNull
    private MarkerOptions getMarkerOptions(PlaceModel placeModel) {
        // Add a marker for each place near the device's current location, with an info window showing place information.
        String snippet = placeModel.getDescription() != null ?
                            placeModel.getAddress() + "\n" + placeModel.getDescription() :
                            placeModel.getAddress();
        List<Integer> placeTypes = placeModel.getPlaceTypes();

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(placeModel.getLat(), placeModel.getLon()))
                .title(placeModel.getName())
                .snippet(snippet);

        int markerResourceId = MarkerIconMapper.getMarkerResourceId(placeTypes);
        if (markerResourceId != MarkerIconMapper.DEFAULT_MARKER_ID) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(markerResourceId));
        }

        return markerOptions;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLocationPermissionAuthorized()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
    }

    private boolean isLocationPermissionAuthorized() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isLocationPermissionAuthorized()) {
            LatLng latLng = getLastKnownCurrentLocation();
            if (latLng != null) {
                onMyLocationFound(latLng);
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void onMyLocationFound(LatLng latLng) {
        if (mMyLocationMarker != null) {
            mMyLocationMarker.remove();
        }
        mMyLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(getString(R.string.my_location_marker_text)));

        mMyLocationPlaceModel = new PlaceModel.Builder()
                .latLon(latLng.latitude, latLng.longitude)
                .name(getString(R.string.my_location_marker_text))
                .build();

        mMyLocationMarker.setTag(mMyLocationPlaceModel);
        animateCamera(latLng);
        displayPointsOfInterest();
    }

    @SuppressWarnings("MissingPermission")
    @NonNull
    private LatLng getLastKnownCurrentLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return location != null ? new LatLng(location.getLatitude(), location.getLongitude()) : null;
    }

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        onMyLocationFound(latLng);
        Log.d(getClass().getName(), "onLocationChanged(), location = " + location);
    }

    private void animateCamera(LatLng latLng) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(POINTS_OF_INTEREST_ZOOM_LEVEL)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_toggle_list:
                // Change the toggle text to reflect the list visibility change that is about to happen.
                item.setTitle(mPlacesContainer.getVisibility() == View.GONE ? getString(R.string.hide_list): getString(R.string.show_list));
                // Toggle the list visibility state between gone and visible.
                mPlacesContainer.setVisibility(mPlacesContainer.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                break;
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else{
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                Toast.makeText(this, "Hamburgers!!!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onPlaceSelected(PlaceModel placeModel) {
        // Center the camera on the corresponding map marker.
        animateCamera(new LatLng(placeModel.getLat(), placeModel.getLon()));
        Marker marker = getCorrespondingMarker(placeModel);
        if (marker != null) {
            marker.showInfoWindow();
        }
        mPlacesAdapter.setSelectedPlace(placeModel);
    }

    private Marker getCorrespondingMarker(PlaceModel placeModel) {
        for (Marker marker : mMarkers) {
            Object tag = marker.getTag();
            if (tag != null && tag.equals(placeModel)) {
                return marker;
            }
        }
        return null;
    }
}
