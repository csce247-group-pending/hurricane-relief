package com.pending.model;

import java.util.ArrayList;
import java.util.UUID;

public class Shelter {
    private UUID id;
    private String name;
    private Location location;
    private Contact contactInfo;
    private int capactiy;
    private ArrayList<Volunteer> volunteers;
    private ArrayList<Resource> resources;
    private ShelterStatus status;

    public Shelter(UUID id, String name, Location location,
        int capacity, ArrayList<Resource> resources) {
        
    }

    public Shelter(String name, Location location,
        int capacity, ArrayList<Resource> resources) {
        
    }

    public void addVolunteer(Volunteer volunteer) {

    }

    public void removeVolunteer(Volunteer volunteer) {

    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {

    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Contact contactInfo) {

    }

    public int getCapactiy() {
        return capactiy;
    }

    public void setCapactiy(int capactiy) {

    }

    public ArrayList<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(ArrayList<Volunteer> volunteers) {

    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {

    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {

    }


}
