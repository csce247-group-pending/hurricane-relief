package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeJSONShelter;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ShelterStatus;

import java.util.ArrayList;
import java.util.UUID;

public class Shelter extends Serializable {
    public static String file = JSONFile.SHELTERS.getFilename();

    private String name;
    private Location location;
    private UUID locationUUIDReference;
    private Contact contactInfo;
    private int capacity;
    private int occupancy;

    private ArrayList<Volunteer> volunteers;
    private ArrayList<UUID> volunteersByUUID;

    private ArrayList<UUID> resourcesByUUID;

    private ShelterStatus status;

    private ShelterManager manager;
    private UUID managerUUIDReference;

    public Shelter(String name, Location location,
            int capacity) {
        this(name, location, capacity, new ArrayList<>(), ShelterStatus.OPEN);
    }

    public Shelter(UUID id, String name, Location location,
        int capacity, ArrayList<Volunteer > volunteers, ShelterStatus status) {
        this(name, location, capacity, volunteers, status);
        this.id = id;
    }

    public Shelter(String name, Location location,
            int capacity, ArrayList<Volunteer> volunteers, ShelterStatus status) {
        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupancy = 0;
        this.volunteers = volunteers == null
                ? new ArrayList<Volunteer>()
                : new ArrayList<Volunteer>(volunteers);
        this.status = status;

        volunteersByUUID = new ArrayList<>();

        serializeBehavior = new SerializeJSONShelter(this);
    }

    public Shelter() {
        this("", new Location(), 0, new ArrayList<>(), ShelterStatus.OPEN);
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

    public UUID getLocationUUIDReference() {
        return locationUUIDReference;
    }

    public void setLocationUUIDReference(UUID locationUUIDReference) {
        this.locationUUIDReference = locationUUIDReference;
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

    public ArrayList<UUID> getVolunteersUUIDReferecne() {
        return volunteersByUUID;
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

    public ArrayList<UUID> getResourcesUUIDReference() {
        return resourcesByUUID;
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    public ShelterManager getManager() {
        return manager;
    }

    public void setManager(ShelterManager manager) {
        this.manager = manager;
    }

    public UUID getManagerUUIDReference() {
        return managerUUIDReference;
    }

    public void setManagerUUIDReference(UUID managerUUIDReference) {
        this.managerUUIDReference = managerUUIDReference;
    }
}