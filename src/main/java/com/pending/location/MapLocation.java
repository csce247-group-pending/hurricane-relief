package com.pending.location;

import com.pending.model.data.serializables.Location;

/**
 * Represents an address together with its latitude and longitude coordinates.
 */
public class MapLocation {

    /** The application's address object. */
    private final Location location;

    /** The geographic coordinates for the address. */
    private final Coordinates coordinates;

    /**
     * Creates a map location from an address and its coordinates.
     *
     * @param location the address
     * @param coordinates the latitude and longitude of the address
     */
    public MapLocation(Location location, Coordinates coordinates) {
        this.location = location;
        this.coordinates = coordinates;
    }

    /**
     * Gets the address associated with this map location.
     *
     * @return the address
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the geographic coordinates associated with this location.
     *
     * @return the latitude and longitude coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the address as readable text.
     *
     * @return the formatted address
     */
    @Override
    public String toString() {
        return location.toString();
    }
}