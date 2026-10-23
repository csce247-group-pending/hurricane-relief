package com.pending.model.data.serializables;

import com.pending.model.data.serializables.behaviors.SerializeJSONShelter;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ShelterStatus;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.ReliefType;

public class Shelter extends Serializable {
    public static String file = JSONFile.SHELTERS.getFilename();

    private String name;
    private Location location;
    private UUID locationUUIDReference;
    private Contact contactInfo;
    private int capacity;
    private ArrayList<LivingBeing> occupants;

    private ArrayList<Volunteer> volunteers;
    private ArrayList<UUID> volunteersByUUID;

    private ShelterStatus status;

    private boolean accomodatesPets;

    private ShelterManager manager;
    private UUID managerUUIDReference;

    private UUID contactUUIDReference;

    public Shelter(UUID id, String name, Location location,
        int capacity, ArrayList<Volunteer> volunteers, ShelterStatus status, Contact contactInfo,
        boolean accomodatesPets) {
        this(name, location, capacity, volunteers, status, contactInfo, accomodatesPets);
        this.id = id;
    }

    public Shelter(String name, Location location,
            int capacity, ArrayList<Volunteer> volunteers, ShelterStatus status,
            Contact contactInfo, boolean accomodatesPets) {
        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupants = new ArrayList<>();
        this.volunteers = volunteers == null
                ? new ArrayList<Volunteer>()
                : new ArrayList<Volunteer>(volunteers);
        this.status = status;
        this.contactInfo = contactInfo;
        this.accomodatesPets = accomodatesPets;

        contactUUIDReference = UUID.randomUUID();

        volunteersByUUID = new ArrayList<>();

        serializeBehavior = new SerializeJSONShelter(this);
    }

    public Shelter() {
        this("", new Location(), 0, new ArrayList<>(), ShelterStatus.OPEN, new Contact(), false);
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

    public ArrayList<LivingBeing> getOccupants() {
        return occupants;
    }

    public boolean addOccupant(LivingBeing occupant) {
        return occupants.add(occupant);
    }

    public boolean removeOccupant(LivingBeing occupant) {
        return occupants.remove(occupant);
    }

    public ArrayList<UUID> getVolunteersUUIDReference() {
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

    public void checkIn(ArrayList<LivingBeing> livingBeings) {

    }

    @Override
    public String toString() {
        String formattedOccupants = "";
        for (LivingBeing occupant: occupants) {
            formattedOccupants += "\t\t" + occupant + "\n";
        }

        return "Shelter '" + name + "'"
        + "\n\tStatus: " + status
        + "\n\tCapacity: " + occupants.size() + "/" + capacity
        + "\n\tLocation: " + location
        + "\n\tContact Info: " + contactInfo
        + (accomodatesPets ? "\n\tAccomdates Pets" : "")
        + "\n\tOccupants: " + (formattedOccupants.isEmpty() ? "None" : "\n" + formattedOccupants);
    }

    public UUID getContactUUIDReference() {
        return contactUUIDReference;
    }

    public void setContactUUIDReference(UUID contactUUIDReference) {
        this.contactUUIDReference = contactUUIDReference;
    }

    public boolean hasMedicalStaff() {
        for(Volunteer volunteer: volunteers) {
            for(ReliefType reliefSkill: volunteer.getReliefTypes()) {
                if (reliefSkill.equals(ReliefType.MEDICAL_CARE)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean accomodatesPets() {
        return accomodatesPets;
    }

    public void setAccomodatesPets(boolean accomodatesPets) {
        this.accomodatesPets = accomodatesPets;
    }
}