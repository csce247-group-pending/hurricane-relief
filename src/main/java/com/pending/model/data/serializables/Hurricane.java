package com.pending.model.data.serializables;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeJSONHurricane;
import com.pending.model.enums.HurricaneStatus;
import com.pending.model.enums.JSONFile;

/**
 * Represents a hurricane that may affect one or more locations.
 * A hurricane has a name, category, status, and a list of related locations.
 */
public class Hurricane extends Serializable {
    /** The JSON file used to store hurricane data. */
    public static String file = JSONFile.HURRICANES.getFilename();

    private String name;
    private int category;
    private ArrayList<Location> locations;
    private ArrayList<UUID> locationsUUIDReference;
    private HurricaneStatus status;

    /**
     * Creates a hurricane with a generated UUID.
     *
     * @param name the hurricane name
     * @param category the hurricane category
     * @param locations the locations affected by the hurricane
     * @param status the current hurricane status
     */
    public Hurricane(String name, int category,
            ArrayList<Location> locations, HurricaneStatus status) {

        this.name = name;
        this.category = category;
        this.locations = locations;
        this.status = status;

        locationsUUIDReference = new ArrayList<>();
        serializeBehavior = new SerializeJSONHurricane(this);
    }

    /**
     * Creates a hurricane with a specified UUID.
     *
     * @param id the hurricane UUID
     * @param name the hurricane name
     * @param category the hurricane category
     * @param startDate the hurricane start date
     * @param endDate the hurricane end date
     * @param locations the locations affected by the hurricane
     * @param status the current hurricane status
     */
    public Hurricane(UUID id, String name, int category,
            LocalDateTime startDate, LocalDateTime endDate,
            ArrayList<Location> locations, HurricaneStatus status) {

        this(name, category, locations, status);
        this.id = id;
    }

    /**
     * Creates a default hurricane with category five and passed status.
     */
    public Hurricane() {
        this("", 5, new ArrayList<>(), HurricaneStatus.PASSED);
    }

    /**
     * Gets the hurricane name.
     *
     * @return the hurricane name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the hurricane name.
     *
     * @param name the new hurricane name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the hurricane category.
     *
     * @return the hurricane category
     */
    public int getCategory() {
        return category;
    }

    /**
     * Sets the hurricane category.
     *
     * @param category the new hurricane category
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Gets the locations affected by the hurricane.
     *
     * @return the list of affected locations
     */
    public ArrayList<Location> getLocations() {
        return locations;
    }

    /**
     * Adds an affected location.
     *
     * @param location the location to add
     */
    public void addLocation(Location location) {
        locations.add(location);
    }

    /**
     * Removes an affected location.
     *
     * @param location the location to remove
     */
    public void removeLocation(Location location) {
        locations.remove(location);
    }

    /**
     * Gets UUID references for affected locations.
     *
     * @return the list of location UUID references
     */
    public ArrayList<UUID> getLocationsUUIDReference() {
        return locationsUUIDReference;
    }

    /**
     * Sets UUID references for affected locations.
     *
     * @param locationsUUIDReference the location UUID references
     */
    public void setLocationsUUIDReference(
            ArrayList<UUID> locationsUUIDReference) {

        this.locationsUUIDReference = locationsUUIDReference;
    }

    /**
     * Gets the hurricane status.
     *
     * @return the current hurricane status
     */
    public HurricaneStatus getStatus() {
        return status;
    }

    /**
     * Sets the hurricane status.
     *
     * @param status the new hurricane status
     */
    public void setStatus(HurricaneStatus status) {
        this.status = status;
    }
}