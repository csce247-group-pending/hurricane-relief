package com.pending.model.data.serializables;

import com.pending.model.HurricaneStatus;
import com.pending.model.JSONFile;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observer;
import java.util.UUID;

public class Hurricane extends Serializable {
    public static String file = JSONFile.HURRICANES.getFilename();
    private String name;
    private int category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ArrayList<Location> locations;
    private HurricaneStatus status;
    private ArrayList<Observer> observers;

    public Hurricane(String name, String category, LocalDateTime startDate,
        ArrayList<Location> locations, HurricaneStatus status) {

        
    }

    public Hurricane(UUID id, String name, String category, LocalDateTime startDate,
        ArrayList<Location> locations, HurricaneStatus status) {

        
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        
    }

    @Override
    public JSONObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {

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

    public HurricaneStatus getStatus() {
        return status;
    }

    public void setStatus(HurricaneStatus status) {
        this.status = status;
    }
}
