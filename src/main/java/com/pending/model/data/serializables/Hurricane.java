package com.pending.model.data.serializables;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.HurricaneStatus;
import com.pending.model.enums.JSONFile;

public class Hurricane extends Serializable {
    public static String file = JSONFile.HURRICANES.getFilename();
    private String name;
    private int category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ArrayList<Location> locations;
    private ArrayList<UUID> locationsUUIDReference;
    private HurricaneStatus status;

    public Hurricane(String name, int category, LocalDateTime startDate, LocalDateTime endDate,
                     ArrayList<Location> locations, HurricaneStatus status) {
        this.name = name;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.locations = locations;
        this.status = status;

        locationsUUIDReference = new ArrayList<>();

        serializeBehavior = new SerializeJSONHurricane(this);
    }

    public Hurricane(UUID id, String name, int category, LocalDateTime startDate, LocalDateTime endDate,
                     ArrayList<Location> locations, HurricaneStatus status) {
        this(name, category, startDate, endDate, locations, status);
        this.id = id;
    }

    public Hurricane() {
        this("", 5, LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), HurricaneStatus.PASSED);
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

