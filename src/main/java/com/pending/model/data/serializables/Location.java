package com.pending.model.data.serializables;

import java.util.UUID;

import com.pending.location.Coordinates;
import com.pending.location.MapTools;
import com.pending.model.data.serializables.behaviors.SerializeJSONLocation;

/**
 * Represents a physical address used by users, shelters, and relief requests.
 * A location can also be converted into map coordinates through MapTools.
 */
public class Location extends Serializable {
    /** The JSON file used to store location data. */
    public static final String file = "json/locations.json";

    private String state;
    private String city;
    private String street;
    private String zip;

    /**
     * Creates a location with address information.
     *
     * @param state the state abbreviation or name
     * @param city the city name
     * @param street the street address
     * @param zip the ZIP code
     */
    public Location(String state, String city, String street, String zip) {
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;

        serializeBehavior = new SerializeJSONLocation(this);
    }

    /**
     * Creates a location with a specified UUID.
     *
     * @param id the location UUID
     * @param state the state abbreviation or name
     * @param city the city name
     * @param street the street address
     * @param zip the ZIP code
     */
    public Location(UUID id, String state, String city,
            String street, String zip) {

        this(state, city, street, zip);
        this.id = id;
    }

    /**
     * Creates an empty location with default values.
     */
    public Location() {
        this("", "", "", "");
    }

    /**
     * Checks whether this location is in a specified state.
     *
     * @param state the state to compare
     * @return true if the states match; otherwise false
     */
    public boolean isInState(String state) {
        return state.equals(this.state);
    }

    /**
     * Checks whether this location is in a specified city.
     *
     * @param city the city to compare
     * @return true if the cities match; otherwise false
     */
    public boolean isInCity(String city) {
        return city.equals(this.city);
    }

    /**
     * Checks whether this location has a specified ZIP code.
     *
     * @param zipcode the ZIP code to compare
     * @return true if the ZIP codes match; otherwise false
     */
    public boolean isInZip(String zipcode) {
        return zipcode.equals(this.zip);
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the new state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets the city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.
     *
     * @param city the new city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the street address.
     *
     * @return the street address
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address.
     *
     * @param street the new street address
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Gets the ZIP code.
     *
     * @return the ZIP code
     */
    public String getZip() {
        return zip;
    }

    /**
     * Sets the ZIP code.
     *
     * @param zip the new ZIP code
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * Gets map coordinates for this location.
     *
     * @return the coordinates, or null if the map request fails
     */
    public Coordinates getCoordinates() {
        try {
            return MapTools.getInstance().getCoordinates(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns the address in a readable format.
     *
     * @return the formatted street, city, state, and ZIP code
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s %s",
                street, city, state, zip);
    }
}