package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeJSONHurricane;
import com.pending.model.enums.HurricaneStatus;
import com.pending.model.enums.JSONFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.HurricaneStatus;
import com.pending.model.enums.JSONFile;

public class Hurricane extends Serializable {
    public static String file = JSONFile.HURRICANES.getFilename();
    private String name;
    private int category;
    private ArrayList<Location> locations;
    private ArrayList<UUID> locationsUUIDReference;
    private HurricaneStatus status;

    public Hurricane(String name, int category,
                     ArrayList<Location> locations, HurricaneStatus status) {
        this.name = name;
        this.category = category;
        this.locations = locations;
        this.status = status;

        locationsUUIDReference = new ArrayList<>();

        serializeBehavior = new SerializeJSONHurricane(this);
    }

    public Hurricane(UUID id, String name, int category, LocalDateTime startDate, LocalDateTime endDate,
                     ArrayList<Location> locations, HurricaneStatus status) {
        this(name, category, locations, status);
        this.id = id;
    }

    public Hurricane() {
        this("", 5, new ArrayList<>(), HurricaneStatus.PASSED);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
    }

    public ArrayList<UUID> getLocationsUUIDReference() {
        return locationsUUIDReference;
    }

    public void setLocationsUUIDReference(ArrayList<UUID> locationsUUIDReference) {
        this.locationsUUIDReference = locationsUUIDReference;
    }

    public HurricaneStatus getStatus() {
        return status;
    }

    public void setStatus(HurricaneStatus status) {
        this.status = status;
    }
}

