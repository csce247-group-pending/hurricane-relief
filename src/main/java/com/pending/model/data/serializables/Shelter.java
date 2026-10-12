package com.pending.model.data.serializables;

import com.pending.model.JSONFile;
import com.pending.model.ShelterStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Shelter extends Serializable {
    public static String file = JSONFile.SHELTERS.getFilename();

    private String name;
    private Location location;
    private Contact contactInfo;
    private int capacity;
    private int occupancy;
    private ArrayList<Volunteer> volunteers;
    private ArrayList<Resource> resources;
    private ShelterStatus status;

    public Shelter(String name, Location location,
            int capacity, ArrayList<Resource> resources) {
        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupancy = 0;
        this.volunteers = new ArrayList<Volunteer>();
        this.resources = resources == null
                ? new ArrayList<Resource>()
                : new ArrayList<Resource>(resources);
        this.status = ShelterStatus.OPEN;
    }

    public Shelter(UUID id, String name, Location location,
        int capacity, ArrayList<Volunteer > volunteers,
                ArrayList < Resource> resources, ShelterStatus status) {
        super();
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupancy = 0;
        this.volunteers = volunteers == null
                ? new ArrayList<Volunteer>()
                : new ArrayList<Volunteer>(volunteers);
        this.resources = resources == null
                ? new ArrayList<Resource>()
                : new ArrayList<Resource>(resources);
        this.status = status;
    }

    public Shelter(String name, Location location,
            int capacity, ArrayList<Volunteer> volunteers,
            ArrayList<Resource> resources, ShelterStatus status) {
        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupancy = 0;
        this.volunteers = volunteers == null
                ? new ArrayList<Volunteer>()
                : new ArrayList<Volunteer>(volunteers);
        this.resources = resources == null
                ? new ArrayList<Resource>()
                : new ArrayList<Resource>(resources);
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public ArrayList<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(ArrayList<Volunteer> volunteers) {
        this.volunteers = volunteers == null
                ? new ArrayList<Volunteer>()
                : new ArrayList<Volunteer>(volunteers);
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

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources == null
                ? new ArrayList<Resource>()
                : new ArrayList<Resource>(resources);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("name", name);
        jsonObject.put("location", location.serialize());
        jsonObject.put("capacity", capacity);
        jsonObject.put("occupancy", occupancy);

        JSONArray volunteers = new JSONArray();
        for (Volunteer volunteer: this.volunteers) {
            volunteers.add(volunteer.getId().toString());
        }

        return null;
    }

    public void deserialize(JSONObject jsonObject) {

    }
}