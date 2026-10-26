package com.pending.location;

/**
 * Stores a geographic position using latitude and longitude values.
 */
public class Coordinates {
    /** The north-to-south position in decimal degrees. */
    private double latitude;

    /** The east-to-west position in decimal degrees. */
    private double longitude;

    /**
     * Creates geographic coordinates.
     *
     * @param latitude the latitude in decimal degrees
     * @param longitude the longitude in decimal degrees
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude in decimal degrees
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude in decimal degrees
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns coordinates in a readable latitude-and-longitude format.
     *
     * @return the latitude followed by the longitude
     */
    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }
}