package com.pending.location;

/**
 * Represents a street address with street, city, state, and ZIP code fields.
 */
public class Address {
    /** The street address. */
    private String street;

    /** The city name. */
    private String city;

    /** The state abbreviation or name. */
    private String state;

    /** The ZIP code. */
    private String zip;

    /**
     * Creates an address.
     *
     * @param street the street address
     * @param city the city
     * @param state the state
     * @param zip the ZIP code
     */
    public Address(String street, String city, String state, String zip) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
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
     * Gets the city.
     *
     * @return the city name
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the state.
     *
     * @return the state abbreviation or name
     */
    public String getState() {
        return state;
    }

    /**
     * Gets the ZIP code.
     *
     * @return the ZIP code
     */
    public String getZip() {
        return zip;
    }

    // public Coordinates getCoordinates() {
    //     try {
    //         // return MapTools.getInstance().getCoordinates(this);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    /**
     * Returns this address in a readable format.
     *
     * @return the formatted street, city, state, and ZIP code
     */
    @Override
    public String toString() {
        return String.format("%s, %s, %s %s", street, city, state, zip);
    }
}