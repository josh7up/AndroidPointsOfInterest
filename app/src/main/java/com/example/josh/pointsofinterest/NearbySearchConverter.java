package com.example.josh.pointsofinterest;

import com.example.josh.pointsofinterest.rest.nearby.NearbySearchResults;
import com.example.josh.pointsofinterest.rest.nearby.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NearbySearchConverter {

    private static final List<String> EXCLUDED_PLACE_TYPES = Arrays.asList(
        "administrative_area_level_1",
        "administrative_area_level_2",
        "administrative_area_level_3",
        "administrative_area_level_4",
        "administrative_area_level_5",
        "colloquial_area",
        "country",
        "locality",
        "political",
        "postal_code",
        "premise",
        "subpremise"
    );

    public List<PlaceModel> getPlaceModels(NearbySearchResults nearbySearchResults) {
        List<PlaceModel> placeModels = new ArrayList<>();

        if (nearbySearchResults.getResults() != null) {
            for (Result result : nearbySearchResults.getResults()) {
                if (isPlaceInteresting(result)) {
                    placeModels.add(buildPlaceModel(result));
                }
            }
        }

        return placeModels;
    }

    private PlaceModel buildPlaceModel(Result result) {
        PlaceModel.Builder builder = new PlaceModel.Builder();

        if (result.getGeometry() != null) {
            if (result.getGeometry().getLocation() != null) {
                double lat = result.getGeometry().getLocation().getLat();
                double lon = result.getGeometry().getLocation().getLng();
                builder.latLon(lat, lon);
            }
        }

        builder.id(result.getPlaceId());
        builder.name(result.getName());
        builder.address(result.getVicinity());
        builder.priceLevel(result.getPriceLevel() != null ? result.getPriceLevel() : -1);

        if (result.getTypes() != null) {
            List<Integer> placeTypes = new ArrayList<>();
            for (String placeType : result.getTypes()) {
                int typeId = PlaceTypeMapper.getTypeId(placeType);
                if (typeId != -1) {
                    placeTypes.add(typeId);
                }
            }
            builder.placeTypes(placeTypes);
        }

        if (result.getRating() != null) {
            builder.rating(result.getRating().floatValue());
        }

        return builder.build();
    }

    private boolean isPlaceInteresting(Result placeResult) {
        if (placeResult.getTypes() != null) {
            for (String placeType : placeResult.getTypes()) {
                if (EXCLUDED_PLACE_TYPES.contains(placeType)) {
                    return false;
                }
            }
        }
        return true;
    }
}
