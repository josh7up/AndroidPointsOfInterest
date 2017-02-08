package com.example.josh.pointsofinterest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnPlaceSelectedListener {

    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    private static final long LOCATION_UPDATE_INTERVAL = 1000 * 60 * 5;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 5;
    private static final float POINTS_OF_INTEREST_ZOOM_LEVEL = 16;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private Marker mMyLocationMarker;
    private PlaceModel mMyLocationPlaceModel;

    private List<Marker> mMarkers = new ArrayList<>();

    private Marker mSearchResultMarker;
    private PlaceModel mSearchResultPlaceModel;

    private GeoCalculator mGeoCalculator = new GeoCalculator();
    private PlaceConverter mPlaceConverter = new PlaceConverter();
    private PlaceService mPlaceService;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesRecyclerViewAdapter mPlacesAdapter;
    private PlaceDAO mPlaceDAO;

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

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlacesRecyclerView.setLayoutManager(mLinearLayoutManager);

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

        mPlaceService = new PlaceService(this);
        mPlaceDAO = new PlaceDAO(this);
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
    private void displayPointsOfInterestForMyLocation(final PlaceModel referencePlaceModel) {
        if (mPlaceDAO.hasPlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon())) {
            List<PlaceModel> placeModels = mPlaceDAO.getPlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon());
            updateDistances(referencePlaceModel, placeModels);
            refreshView(referencePlaceModel, placeModels);
        } else {
            PendingResult<PlaceLikelihoodBuffer> nearbyPendingResult = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            nearbyPendingResult.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    List<PlaceModel> placeModels = mPlaceConverter.convert(likelyPlaces);
                    likelyPlaces.release();
                    updateDistances(referencePlaceModel, placeModels);
                    refreshView(referencePlaceModel, placeModels);
                    mPlaceDAO.savePlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon(), placeModels);
                }
            });
        }
    }

    private void displayPointsOfInterest(final PlaceModel referencePlaceModel) {
        // See if there are cached places for the given lat/lon.
        if (mPlaceDAO.hasPlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon())) {
            List<PlaceModel> placeModels = mPlaceDAO.getPlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon());
            updateDistances(referencePlaceModel, placeModels);
            refreshView(referencePlaceModel, placeModels);
        } else {
            mPlaceService.getNearbyPlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon(), new PlaceService.OnNearbyPlacesCallback() {
                @Override
                public void onSuccess(List<PlaceModel> placeModels) {
                    updateDistances(referencePlaceModel, placeModels);
                    refreshView(referencePlaceModel, placeModels);
                    mPlaceDAO.savePlaces(referencePlaceModel.getLat(), referencePlaceModel.getLon(), placeModels);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(getClass().getName(), "Error getting nearby places", t);
                }
            });
        }
    }

    /**
     * Displays new map markers and updates the places list with the given nearbyPlaceModels data.
     *
     * @param referencePlaceModel - the PlaceModel that the nearby locations are in reference to.
     * @param nearbyPlaceModels - The nearby place models to display on the map and list.
     */
    private void refreshView(PlaceModel referencePlaceModel, List <PlaceModel> nearbyPlaceModels) {
        clearMapMarkers();
        mToolbar.setSubtitle("(" + referencePlaceModel.getName() + ")");
        mMarkers = addMarkers(nearbyPlaceModels);
        mPlacesAdapter = new PlacesRecyclerViewAdapter(nearbyPlaceModels, this, this);
        mPlacesRecyclerView.setAdapter(mPlacesAdapter);
    }

    /**
     * Calculates the distance between each placeModel's coordinates and the reference coordinates and
     * updates each placeModel's distance property.
     *
     * @param referencePlaceModel - the PlaceModel that the nearby locations are in reference to.
     * @param nearbyPlaceModels - The models to use for the distance calculations.
     */
    private void updateDistances(PlaceModel referencePlaceModel, List<PlaceModel> nearbyPlaceModels) {
        for (PlaceModel placeModel : nearbyPlaceModels) {
            // Calculate the distance of the placeModel's coordinates from the reference lat/lon.
            double distance = mGeoCalculator.calculateDistance(
                    referencePlaceModel.getLat(),
                    referencePlaceModel.getLon(),
                    placeModel.getLat(),
                    placeModel.getLon());
            placeModel.setDistance(distance);
        }
    }

    /**
     * Adds markers to the map that correspond to the given placeModels.
     * @param placeModels - models that contain the data for adding map markers.
     * @return - The Markers that were added to the map.
     */
    private List<Marker> addMarkers(List<PlaceModel> placeModels) {
        List<Marker> markers = new ArrayList<>();
        for (PlaceModel placeModel : placeModels) {
            Marker marker = mMap.addMarker(getMarkerOptions(placeModel));
            marker.setTag(placeModel);
            markers.add(marker);
        }
        return markers;
    }

    private void clearMapMarkers() {
        for (Marker marker : mMarkers) {
            marker.remove();
        }
        mMarkers.clear();
    }

    private MarkerOptions getMarkerOptions(PlaceModel placeModel) {
        // Add a marker for each place near the device's current location, with an info window showing place information.
        String snippet = placeModel.getDescription() != null ?
                            placeModel.getAddress() + "\n" + placeModel.getDescription() :
                            placeModel.getAddress();

        return new MarkerOptions()
                .position(new LatLng(placeModel.getLat(), placeModel.getLon()))
                .title(placeModel.getName())
                .icon(getMarkerBitmapDescriptor(placeModel.getPlaceTypes()))
                .snippet(snippet);
    }

    /**
     * Return the first custom marker resource that matches any of the place types. Fallback to the default
     * marker resource if there is no match.
     */
    private BitmapDescriptor getMarkerBitmapDescriptor(List<Integer> placeTypes) {
        for (int placeType : placeTypes) {
            int markerResourceId = PlaceTypeMapper.getMarkerResourceId(placeType);
            if (markerResourceId != PlaceTypeMapper.DEFAULT_MARKER_ID) {
                return BitmapDescriptorFactory.fromResource(markerResourceId);
            }
        }
        return BitmapDescriptorFactory.defaultMarker();
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
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
        displayPointsOfInterestForMyLocation(mMyLocationPlaceModel);
    }

    private void onPlaceSearchCompleted(Place place) {
        stopLocationUpdates();

        if (mSearchResultMarker != null) {
            mSearchResultMarker.remove();
        }

        mSearchResultPlaceModel = mPlaceConverter.convert(place);
        mSearchResultMarker = mMap.addMarker(new MarkerOptions()
                .position(place.getLatLng())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title((String) place.getName()));
        mSearchResultMarker.setTag(mMyLocationPlaceModel);

        animateCamera(place.getLatLng());
        displayPointsOfInterest(mSearchResultPlaceModel);
    }

    @SuppressWarnings("MissingPermission")
    @NonNull
    private LatLng getLastKnownCurrentLocation() {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        return location != null ? new LatLng(location.getLatitude(), location.getLongitude()) : null;
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
            case R.id.menu_item_search:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(getClass().getName(), "Error showing the search view.", e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(getClass().getName(), "Error showing the search view.", e);
                }

                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                onPlaceSearchCompleted(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, getString(R.string.place_search_error) + ": " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.e(getClass().getName(), "Place search error: " + status);
            }
        }
    }

    @Override
    public void onPlaceItemSelected(PlaceModel placeModel) {
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

    @Override
    public void onConnectionSuspended(int cause) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
