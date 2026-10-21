package com.pending.model.data.serializables;

import java.util.UUID;

import com.pending.location.Coordinates;
import com.pending.location.MapTools;

public class Location extends Serializable {
    public static final String file = "json/locations.json";

    private String state;
    private String city;
    private String street;
    private String zip;

    public Location(String state, String city, String street, String zip) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;

        serializeBehavior = new SerializeJSONLocation(this);
    }

    public Location(UUID id, String state, String city, String street, String zip) {
        this(state, city, street, zip);
        this.id = id;
    }

    public Location() {
        this("", "", "", "");
    }

    public boolean isInState(String state) {
        return state.equals(this.state);
    }

    public boolean isInCity(String city) {
        return city.equals(this.city);
    }

    public boolean isInZip(String zipcode) {
        return zipcode.equals(this.zip);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Coordinates getCoordinates() {
        try {
            return MapTools.getInstance().getCoordinates(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return String.format("%s, %s, %s %s", street, city, state, zip);
    }
}
