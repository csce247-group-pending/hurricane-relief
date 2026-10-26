package com.pending.location;

import java.util.ArrayList;

/**
 * Represents a driving route between two map locations.
 * The route contains its start and end locations plus coordinate points
 * that can be used to draw the route on a map.
 */
public class DrivingRoute {

    /** The location where the route begins. */
    private final MapLocation startingLocation;

    /** The location where the route ends. */
    private final MapLocation endingLocation;

    /** The coordinate points that make up the route. */
    private final ArrayList<Coordinates> routePoints;

    /**
     * Creates a driving route.
     *
     * @param startingLocation the location where the route begins
     * @param endingLocation the location where the route ends
     * @param routePoints the coordinates used to draw the route
     */
    public DrivingRoute(
            MapLocation startingLocation,
            MapLocation endingLocation,
            ArrayList<Coordinates> routePoints
    ) {
        this.startingLocation = startingLocation;
        this.endingLocation = endingLocation;
        this.routePoints = routePoints;
    }

    /**
     * Gets the location where the route begins.
     *
     * @return the starting map location
     */
    public MapLocation getStartingLocation() {
        return startingLocation;
    }

    /**
     * Gets the location where the route ends.
     *
     * @return the ending map location
     */
    public MapLocation getEndingLocation() {
        return endingLocation;
    }

    /**
     * Gets the coordinate points that make up this route.
     *
     * @return the route coordinate points
     */
    public ArrayList<Coordinates> getRoutePoints() {
        return routePoints;
    }
}