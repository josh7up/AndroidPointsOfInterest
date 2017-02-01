package com.example.josh.pointsofinterest;

import java.io.Serializable;
import java.util.List;

public class PlaceModel implements Serializable {

    private String description;
    private String address;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private String websiteUrl;
    private double lat, lon;
    private String name;
    private List<Integer> placeTypes;
    private String id;
    // Calculated field.
    private double distance;

    /**
     * private constructor used by inner Builder.
     */
    private PlaceModel() {
    }

    public void setDistance(double distanceMiles) {
        this.distance = distanceMiles;
    }

    public double getDistance() {
        return distance;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public float getRating() {
        return rating;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceModel that = (PlaceModel) o;

        if (Float.compare(that.rating, rating) != 0) return false;
        if (priceLevel != that.priceLevel) return false;
        if (Double.compare(that.lat, lat) != 0) return false;
        if (Double.compare(that.lon, lon) != 0) return false;
        if (Double.compare(that.distance, distance) != 0) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
            return false;
        if (websiteUrl != null ? !websiteUrl.equals(that.websiteUrl) : that.websiteUrl != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (placeTypes != null ? !placeTypes.equals(that.placeTypes) : that.placeTypes != null)
            return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = description != null ? description.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (rating != +0.0f ? Float.floatToIntBits(rating) : 0);
        result = 31 * result + priceLevel;
        result = 31 * result + (websiteUrl != null ? websiteUrl.hashCode() : 0);
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (placeTypes != null ? placeTypes.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public static class Builder {

        private PlaceModel mPlaceModel = new PlaceModel();

        public Builder address(String address) {
            mPlaceModel.address = address;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            mPlaceModel.phoneNumber = phoneNumber;
            return this;
        }

        public Builder rating(float rating) {
            mPlaceModel.rating = rating;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            mPlaceModel.websiteUrl = websiteUrl;
            return this;
        }

        public Builder latLon(double lat, double lon) {
            mPlaceModel.lat = lat;
            mPlaceModel.lon = lon;
            return this;
        }

        public Builder name(String name) {
            mPlaceModel.name = name;
            return this;
        }

        public Builder placeTypes(List<Integer> placeTypes) {
            mPlaceModel.placeTypes = placeTypes;
            return this;
        }

        public Builder priceLevel(int priceLevel) {
            mPlaceModel.priceLevel = priceLevel;
            return this;
        }

        public Builder description(String description) {
            mPlaceModel.description = description;
            return this;
        }

        public Builder id(String id) {
            mPlaceModel.id = id;
            return this;
        }

        public PlaceModel build() {
            return mPlaceModel;
        }
    }
}
