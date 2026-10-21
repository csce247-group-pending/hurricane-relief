package com.pending.location;

import com.pending.model.data.serializables.Location;

/*
 * Represents one location after it has been converted
 * into latitude and longitude coordinates.
 */
public class MapLocation {

    private final Location location;
    private final Coordinates coordinates;

    public MapLocation(Location location, Coordinates coordinates) {
        this.location = location;
        this.coordinates = coordinates;
    }

    public Location getLocation() {
        return location;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return location.toString();
    }
}