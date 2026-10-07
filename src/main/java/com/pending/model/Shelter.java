package com.pending.model;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONObject;

public class Shelter extends Serializable {
    private String name;
    private Location location;
    private Contact contactInfo;
    private int capacity;
    private ArrayList<Volunteer> volunteers;
    private ArrayList<Resource> resources;
    private ShelterStatus status;

    public Shelter(UUID id, String name, Location location,
        int capacity, ArrayList<Volunteer> volunteers, ArrayList<Resource> resources,
        ShelterStatus status) {
        super();    //? does this create a new id every time, 
                    //? thus needing to inefficiently overwrite it for pre-existing Shelters?
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.volunteers = volunteers;
        this.resources = resources;
        this.status = status;
    }

    public Shelter(String name, Location location,
        int capacity, ArrayList<Volunteer> volunteers, ArrayList<Resource> resources,
        ShelterStatus status) {
        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.volunteers = volunteers;
        this.resources = resources;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getCapactiy() {
        return capacity;
    }

    public void setCapactiy(int capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(ArrayList<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
    }

    public void removeVolunteer(Volunteer volunteer) {
        volunteers.remove(volunteer);
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    @Override
    public JSONObject serialize() {
        return null;
    }

    @Override
    public void deserialize(JSONObject object) {
    }

}
