package com.example.josh.pointsofinterest;

import com.google.android.gms.location.places.Place;

import java.util.HashMap;
import java.util.List;

public class PlaceTypeMapper {

    public static final int DEFAULT_MARKER_ID = -1;
    public static final int DEFAULT_TYPE_ID = -1;
    private static final HashMap<Integer, Integer> typeIdToResourceIdMap = new HashMap<>();
    private static final HashMap<String, Integer> typeNameToTypeIdMap = new HashMap<>();

    static {
        typeNameToTypeIdMap.put("accounting", Place.TYPE_ACCOUNTING);
        typeNameToTypeIdMap.put("airport", Place.TYPE_AIRPORT);
        typeNameToTypeIdMap.put("amusement_park", Place.TYPE_AMUSEMENT_PARK);
        typeNameToTypeIdMap.put("aquarium", Place.TYPE_AQUARIUM);
        typeNameToTypeIdMap.put("art_gallery", Place.TYPE_ART_GALLERY);
        typeNameToTypeIdMap.put("atm", Place.TYPE_ATM);
        typeNameToTypeIdMap.put("bakery", Place.TYPE_BAKERY);
        typeNameToTypeIdMap.put("bank", Place.TYPE_BANK);
        typeNameToTypeIdMap.put("bar", Place.TYPE_BAR);
        typeNameToTypeIdMap.put("beauty_salon", Place.TYPE_BEAUTY_SALON);
        typeNameToTypeIdMap.put("bicycle_store", Place.TYPE_BICYCLE_STORE);
        typeNameToTypeIdMap.put("book_store", Place.TYPE_BOOK_STORE);
        typeNameToTypeIdMap.put("bowling_alley", Place.TYPE_BOWLING_ALLEY);
        typeNameToTypeIdMap.put("bus_station", Place.TYPE_BUS_STATION);
        typeNameToTypeIdMap.put("cafe", Place.TYPE_CAFE);
        typeNameToTypeIdMap.put("campground", Place.TYPE_CAMPGROUND);
        typeNameToTypeIdMap.put("car_dealer", Place.TYPE_CAR_DEALER);
        typeNameToTypeIdMap.put("car_rental", Place.TYPE_CAR_RENTAL);
        typeNameToTypeIdMap.put("car_repair", Place.TYPE_CAR_REPAIR);
        typeNameToTypeIdMap.put("car_wash", Place.TYPE_CAR_WASH);
        typeNameToTypeIdMap.put("casino", Place.TYPE_CASINO);
        typeNameToTypeIdMap.put("cemetery", Place.TYPE_CEMETERY);
        typeNameToTypeIdMap.put("church", Place.TYPE_CHURCH);
        typeNameToTypeIdMap.put("city_hall", Place.TYPE_CITY_HALL);
        typeNameToTypeIdMap.put("clothing_store", Place.TYPE_CLOTHING_STORE);
        typeNameToTypeIdMap.put("convenience_store", Place.TYPE_CONVENIENCE_STORE);
        typeNameToTypeIdMap.put("courthouse", Place.TYPE_COURTHOUSE);
        typeNameToTypeIdMap.put("dentist", Place.TYPE_DENTIST);
        typeNameToTypeIdMap.put("doctor", Place.TYPE_DOCTOR);
        typeNameToTypeIdMap.put("electrician", Place.TYPE_ELECTRICIAN);
        typeNameToTypeIdMap.put("electronics_store", Place.TYPE_ELECTRONICS_STORE);
        typeNameToTypeIdMap.put("embassy", Place.TYPE_EMBASSY);
        typeNameToTypeIdMap.put("establishment", Place.TYPE_ESTABLISHMENT);
        typeNameToTypeIdMap.put("florist", Place.TYPE_FLORIST);
        typeNameToTypeIdMap.put("food", Place.TYPE_FOOD);
        typeNameToTypeIdMap.put("funeral_home", Place.TYPE_FUNERAL_HOME);
        typeNameToTypeIdMap.put("furniture_store", Place.TYPE_FURNITURE_STORE);
        typeNameToTypeIdMap.put("gas_station", Place.TYPE_GAS_STATION);
        typeNameToTypeIdMap.put("gym", Place.TYPE_GYM);
        typeNameToTypeIdMap.put("hair_care", Place.TYPE_HAIR_CARE);
        typeNameToTypeIdMap.put("hardware_store", Place.TYPE_HARDWARE_STORE);
        typeNameToTypeIdMap.put("hindu_temple", Place.TYPE_HINDU_TEMPLE);
        typeNameToTypeIdMap.put("home_goods_store", Place.TYPE_HOME_GOODS_STORE);
        typeNameToTypeIdMap.put("hospital", Place.TYPE_HOSPITAL);
        typeNameToTypeIdMap.put("insurance_agency", Place.TYPE_INSURANCE_AGENCY);
        typeNameToTypeIdMap.put("jewelry_store", Place.TYPE_JEWELRY_STORE);
        typeNameToTypeIdMap.put("laundry", Place.TYPE_LAUNDRY);
        typeNameToTypeIdMap.put("lawyer", Place.TYPE_LAWYER);
        typeNameToTypeIdMap.put("library", Place.TYPE_LIBRARY);
        typeNameToTypeIdMap.put("liquor_store", Place.TYPE_LIQUOR_STORE);
        typeNameToTypeIdMap.put("local_government_office", Place.TYPE_LOCAL_GOVERNMENT_OFFICE);
        typeNameToTypeIdMap.put("locksmith", Place.TYPE_LOCKSMITH);
        typeNameToTypeIdMap.put("lodging", Place.TYPE_LODGING);
        typeNameToTypeIdMap.put("meal_delivery", Place.TYPE_MEAL_DELIVERY);
        typeNameToTypeIdMap.put("meal_takeaway", Place.TYPE_MEAL_TAKEAWAY);
        typeNameToTypeIdMap.put("mosque", Place.TYPE_MOSQUE);
        typeNameToTypeIdMap.put("movie_rental", Place.TYPE_MOVIE_RENTAL);
        typeNameToTypeIdMap.put("movie_theater", Place.TYPE_MOVIE_THEATER);
        typeNameToTypeIdMap.put("moving_company", Place.TYPE_MOVING_COMPANY);
        typeNameToTypeIdMap.put("museum", Place.TYPE_MUSEUM);
        typeNameToTypeIdMap.put("night_club", Place.TYPE_NIGHT_CLUB);
        typeNameToTypeIdMap.put("painter", Place.TYPE_PAINTER);
        typeNameToTypeIdMap.put("park", Place.TYPE_PARK);
        typeNameToTypeIdMap.put("parking", Place.TYPE_PARKING);
        typeNameToTypeIdMap.put("pet_store", Place.TYPE_PET_STORE);
        typeNameToTypeIdMap.put("pharmacy", Place.TYPE_PHARMACY);
        typeNameToTypeIdMap.put("physiotherapist", Place.TYPE_PHYSIOTHERAPIST);
        typeNameToTypeIdMap.put("plumber", Place.TYPE_PLUMBER);
        typeNameToTypeIdMap.put("police", Place.TYPE_POLICE);
        typeNameToTypeIdMap.put("post_office", Place.TYPE_POST_OFFICE);
        typeNameToTypeIdMap.put("real_estate_agency", Place.TYPE_REAL_ESTATE_AGENCY);
        typeNameToTypeIdMap.put("restaurant", Place.TYPE_RESTAURANT);
        typeNameToTypeIdMap.put("roofing_contractor", Place.TYPE_ROOFING_CONTRACTOR);
        typeNameToTypeIdMap.put("rv_park", Place.TYPE_RV_PARK);
        typeNameToTypeIdMap.put("school", Place.TYPE_SCHOOL);
        typeNameToTypeIdMap.put("shoe_store", Place.TYPE_SHOE_STORE);
        typeNameToTypeIdMap.put("shopping_mall", Place.TYPE_SHOPPING_MALL);
        typeNameToTypeIdMap.put("spa", Place.TYPE_SPA);
        typeNameToTypeIdMap.put("stadium", Place.TYPE_STADIUM);
        typeNameToTypeIdMap.put("storage", Place.TYPE_STORAGE);
        typeNameToTypeIdMap.put("store", Place.TYPE_STORE);
        typeNameToTypeIdMap.put("subway_station", Place.TYPE_SUBWAY_STATION);
        typeNameToTypeIdMap.put("synagogue", Place.TYPE_SYNAGOGUE);
        typeNameToTypeIdMap.put("taxi_stand", Place.TYPE_TAXI_STAND);
        typeNameToTypeIdMap.put("train_station", Place.TYPE_TRAIN_STATION);
        typeNameToTypeIdMap.put("transit_station", Place.TYPE_TRANSIT_STATION);
        typeNameToTypeIdMap.put("travel_agency", Place.TYPE_TRAVEL_AGENCY);
        typeNameToTypeIdMap.put("university", Place.TYPE_UNIVERSITY);
        typeNameToTypeIdMap.put("veterinary_care", Place.TYPE_VETERINARY_CARE);
        typeNameToTypeIdMap.put("zoo", Place.TYPE_ZOO);

        typeIdToResourceIdMap.put(Place.TYPE_ACCOUNTING, R.drawable.accountancy);
        typeIdToResourceIdMap.put(Place.TYPE_AIRPORT, R.drawable.travel);
        typeIdToResourceIdMap.put(Place.TYPE_AMUSEMENT_PARK, R.drawable.entertainment);
        typeIdToResourceIdMap.put(Place.TYPE_AQUARIUM, R.drawable.museums);
        typeIdToResourceIdMap.put(Place.TYPE_ART_GALLERY, R.drawable.arts_crafts);
        typeIdToResourceIdMap.put(Place.TYPE_ATM, R.drawable.financial_services);
        typeIdToResourceIdMap.put(Place.TYPE_BAKERY, R.drawable.cake_shop);
        typeIdToResourceIdMap.put(Place.TYPE_BANK, R.drawable.financial_services);
        typeIdToResourceIdMap.put(Place.TYPE_BAR, R.drawable.bars);
        typeIdToResourceIdMap.put(Place.TYPE_BEAUTY_SALON, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_BICYCLE_STORE, R.drawable.sporting_goods);
        typeIdToResourceIdMap.put(Place.TYPE_BOOK_STORE, R.drawable.books_media);
        typeIdToResourceIdMap.put(Place.TYPE_BOWLING_ALLEY, R.drawable.entertainment);
        typeIdToResourceIdMap.put(Place.TYPE_BUS_STATION, R.drawable.transport);
        typeIdToResourceIdMap.put(Place.TYPE_CAFE, R.drawable.coffee_n_tea);
        typeIdToResourceIdMap.put(Place.TYPE_CAMPGROUND, R.drawable.parks);
        typeIdToResourceIdMap.put(Place.TYPE_CAR_DEALER, R.drawable.automotive);
        typeIdToResourceIdMap.put(Place.TYPE_CAR_RENTAL, R.drawable.travel);
        typeIdToResourceIdMap.put(Place.TYPE_CAR_REPAIR, R.drawable.automotive);
        typeIdToResourceIdMap.put(Place.TYPE_CAR_WASH, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_CASINO, R.drawable.entertainment);
        typeIdToResourceIdMap.put(Place.TYPE_CHURCH, R.drawable.religious_organizations);
        typeIdToResourceIdMap.put(Place.TYPE_CITY_HALL, R.drawable.government);
        typeIdToResourceIdMap.put(Place.TYPE_CLOTHING_STORE, R.drawable.clothings);
        typeIdToResourceIdMap.put(Place.TYPE_CONVENIENCE_STORE, R.drawable.shopping);
        typeIdToResourceIdMap.put(Place.TYPE_COURTHOUSE, R.drawable.government);
        typeIdToResourceIdMap.put(Place.TYPE_DENTIST, R.drawable.dental);
        typeIdToResourceIdMap.put(Place.TYPE_DOCTOR, R.drawable.doctors);
        typeIdToResourceIdMap.put(Place.TYPE_ELECTRICIAN, R.drawable.professional);
        typeIdToResourceIdMap.put(Place.TYPE_ELECTRONICS_STORE, R.drawable.electronics);
        typeIdToResourceIdMap.put(Place.TYPE_EMBASSY, R.drawable.government);
        typeIdToResourceIdMap.put(Place.TYPE_FLORIST, R.drawable.gifts_flowers);
        typeIdToResourceIdMap.put(Place.TYPE_FOOD, R.drawable.food);
        typeIdToResourceIdMap.put(Place.TYPE_FUNERAL_HOME, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_FURNITURE_STORE, R.drawable.furniture_stores);
        typeIdToResourceIdMap.put(Place.TYPE_GAS_STATION, R.drawable.automotive);
        typeIdToResourceIdMap.put(Place.TYPE_GYM, R.drawable.sports);
        typeIdToResourceIdMap.put(Place.TYPE_HAIR_CARE, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_HARDWARE_STORE, R.drawable.local_services);
        typeIdToResourceIdMap.put(Place.TYPE_HEALTH, R.drawable.health_medical);
        typeIdToResourceIdMap.put(Place.TYPE_HINDU_TEMPLE, R.drawable.religious_organizations);
        typeIdToResourceIdMap.put(Place.TYPE_HOME_GOODS_STORE, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_HOSPITAL, R.drawable.health_medical);
        typeIdToResourceIdMap.put(Place.TYPE_INSURANCE_AGENCY, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_JEWELRY_STORE, R.drawable.jewelry);
        typeIdToResourceIdMap.put(Place.TYPE_LAUNDRY, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_LAWYER, R.drawable.law);
        typeIdToResourceIdMap.put(Place.TYPE_LIBRARY, R.drawable.libraries);
        typeIdToResourceIdMap.put(Place.TYPE_LIQUOR_STORE, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_LOCAL_GOVERNMENT_OFFICE, R.drawable.government);
        typeIdToResourceIdMap.put(Place.TYPE_LOCKSMITH, R.drawable.local_services);
        typeIdToResourceIdMap.put(Place.TYPE_LODGING, R.drawable.hotels);
        typeIdToResourceIdMap.put(Place.TYPE_MEAL_DELIVERY, R.drawable.food);
        typeIdToResourceIdMap.put(Place.TYPE_MEAL_TAKEAWAY, R.drawable.food);
        typeIdToResourceIdMap.put(Place.TYPE_MOSQUE, R.drawable.religious_organizations);
        typeIdToResourceIdMap.put(Place.TYPE_MOVIE_RENTAL, R.drawable.movies);
        typeIdToResourceIdMap.put(Place.TYPE_MOVIE_THEATER, R.drawable.movies);
        typeIdToResourceIdMap.put(Place.TYPE_MOVING_COMPANY, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_MUSEUM, R.drawable.museums);
        typeIdToResourceIdMap.put(Place.TYPE_NIGHT_CLUB, R.drawable.nightlife);
        typeIdToResourceIdMap.put(Place.TYPE_PAINTER, R.drawable.professional);
        typeIdToResourceIdMap.put(Place.TYPE_PARK, R.drawable.parks);
        typeIdToResourceIdMap.put(Place.TYPE_PET_STORE, R.drawable.pets);
        typeIdToResourceIdMap.put(Place.TYPE_PHARMACY, R.drawable.medical);
        typeIdToResourceIdMap.put(Place.TYPE_POLICE, R.drawable.law);
        typeIdToResourceIdMap.put(Place.TYPE_POST_OFFICE, R.drawable.government);
        typeIdToResourceIdMap.put(Place.TYPE_REAL_ESTATE_AGENCY, R.drawable.real_estate);
        typeIdToResourceIdMap.put(Place.TYPE_RESTAURANT, R.drawable.restaurants);
        typeIdToResourceIdMap.put(Place.TYPE_SCHOOL, R.drawable.schools);
        typeIdToResourceIdMap.put(Place.TYPE_SHOE_STORE, R.drawable.fashion);
        typeIdToResourceIdMap.put(Place.TYPE_SHOPPING_MALL, R.drawable.shopping);
        typeIdToResourceIdMap.put(Place.TYPE_STADIUM, R.drawable.sports);
        typeIdToResourceIdMap.put(Place.TYPE_STORE, R.drawable.business);
        typeIdToResourceIdMap.put(Place.TYPE_STREET_ADDRESS, R.drawable.residential_places);
        typeIdToResourceIdMap.put(Place.TYPE_SUBWAY_STATION, R.drawable.transport);
        typeIdToResourceIdMap.put(Place.TYPE_TRAIN_STATION, R.drawable.transport);
        typeIdToResourceIdMap.put(Place.TYPE_TRANSIT_STATION, R.drawable.transport);
        typeIdToResourceIdMap.put(Place.TYPE_UNIVERSITY, R.drawable.schools);
        typeIdToResourceIdMap.put(Place.TYPE_ZOO, R.drawable.pets);
    }

    /**
     * Returns a place type id that represents the given placeType description.
     * @param placeType A place description, like "campground" and "amusement_park".
     */
    public static int getTypeId(String placeType) {
        Integer typeId = typeNameToTypeIdMap.get(placeType);
        return typeId != null ? typeId : DEFAULT_TYPE_ID;
    }

    /**
     * Returns the resource id of a marker icon that represents the given placeTypeId.
     */
    public static int getMarkerResourceId(int placeTypeId) {
        Integer resourceId = typeIdToResourceIdMap.get(placeTypeId);
        return resourceId != null ? resourceId : DEFAULT_MARKER_ID;
    }
}
