package com.pending.model.data.serializables;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.behaviors.SerializeJSONShelter;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.ShelterStatus;

/**
 * Represents a disaster-relief shelter.
 * A shelter has a location, contact information, capacity, occupants,
 * volunteers, a status, and optional pet accommodations.
 */
public class Shelter extends Serializable {
    /** The JSON file used to store shelter data. */
    public static String file = JSONFile.SHELTERS.getFilename();

    private String name;
    private Location location;
    private Contact contactInfo;
    private int capacity;
    private ArrayList<LivingBeing> occupants;
    private ArrayList<UUID> occupantsByUUID;

    private ArrayList<Volunteer> volunteers;
    private ArrayList<UUID> volunteersByUUID;

    private ShelterStatus status;
    private boolean isPetFriendly;

    private ShelterManager manager;
    
    private UUID locationUUIDReference;
    private UUID managerUUIDReference;
    private UUID contactUUIDReference;

    /**
     * Creates a shelter with a specified UUID.
     *
     * @param id the shelter UUID
     * @param name the shelter name
     * @param location the shelter location
     * @param capacity the maximum number of occupants
     * @param volunteers the volunteers assigned to the shelter
     * @param status the current shelter status
     * @param contactInfo the shelter contact information
     * @param isPetFriendly whether the shelter accepts pets
     */
    public Shelter(UUID id, String name, Location location,
            int capacity, ArrayList<Volunteer> volunteers,
            ShelterStatus status, Contact contactInfo,
            boolean isPetFriendly) {

        this(name, location, capacity, volunteers, status,
                contactInfo, isPetFriendly);
        this.id = id;
    }

    /**
     * Creates a shelter with a generated UUID.
     *
     * @param name the shelter name
     * @param location the shelter location
     * @param capacity the maximum number of occupants
     * @param volunteers the volunteers assigned to the shelter
     * @param status the current shelter status
     * @param contactInfo the shelter contact information
     * @param isPetFriendly whether the shelter accepts pets
     */
    public Shelter(String name, Location location,
            int capacity, ArrayList<Volunteer> volunteers,
            ShelterStatus status, Contact contactInfo,
            boolean isPetFriendly) {

        super();
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.occupants = new ArrayList<>();
        this.occupantsByUUID = new ArrayList<>();
        this.volunteers = volunteers == null
                ? new ArrayList<>()
                : new ArrayList<>(volunteers);
        this.status = status;
        this.contactInfo = contactInfo;
        this.isPetFriendly = isPetFriendly;

        contactUUIDReference = UUID.randomUUID();
        locationUUIDReference = UUID.randomUUID();
        managerUUIDReference = UUID.randomUUID();

        volunteersByUUID = new ArrayList<>();

        serializeBehavior = new SerializeJSONShelter(this);
    }

    /**
     * Creates an empty open shelter with default values.
     */
    public Shelter() {
        this("", new Location(), 0, new ArrayList<>(),
                ShelterStatus.OPEN, new Contact(), false);
    }

    /**
     * Gets the shelter name.
     *
     * @return the shelter name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the shelter name.
     *
     * @param name the new shelter name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the shelter location.
     *
     * @return the shelter location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the shelter location.
     *
     * @param location the new shelter location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the UUID reference for the shelter location.
     *
     * @return the location UUID reference
     */
    public UUID getLocationUUIDReference() {
        return locationUUIDReference;
    }

    /**
     * Sets the UUID reference for the shelter location.
     *
     * @param locationUUIDReference the location UUID reference
     */
    public void setLocationUUIDReference(UUID locationUUIDReference) {
        this.locationUUIDReference = locationUUIDReference;
    }

    /**
     * Gets the shelter contact information.
     *
     * @return the shelter contact information
     */
    public Contact getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the shelter contact information.
     *
     * @param contactInfo the new contact information
     */
    public void setContactInfo(Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the maximum shelter capacity.
     *
     * @return the shelter capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the maximum shelter capacity.
     *
     * @param capacity the new shelter capacity
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Gets the shelter occupants.
     *
     * @return the list of occupants
     */
    public ArrayList<LivingBeing> getOccupants() {
        return occupants;
    }

    public int getNumOccupants() {
        if (occupants == null) { return 0; }

        int count = 0;
        for (LivingBeing occupant: occupants) {
            if (occupant.getType() == LivingBeingType.HUMAN) {
                ++count;
            }
        }
        return count;
    }

    public boolean hasCapacity(int partySize) {
        if (status != ShelterStatus.OPEN) {
            return false;
        }
        return (capacity - getNumOccupants()) >= partySize;
    }

    /**
     * Gets the UUID references for the shelter occupants.
     *
     * @return the list of occupant UUID references
     */
    public ArrayList<UUID> getOccupantsUUIDReference() {
        return occupantsByUUID;
    }

    /**
     * Adds an occupant to the shelter.
     *
     * @param occupant the living being to add
     * @return true if the occupant was added
     */
    public boolean addOccupant(LivingBeing occupant) {
        return occupants.add(occupant);
    }

    /**
     * Removes an occupant from the shelter.
     *
     * @param occupant the living being to remove
     * @return true if the occupant was removed
     */
    public boolean removeOccupant(LivingBeing occupant) {
        return occupants.remove(occupant);
    }

    /**
     * Gets UUID references for the shelter volunteers.
     *
     * @return the volunteer UUID references
     */
    public ArrayList<UUID> getVolunteersUUIDReference() {
        return volunteersByUUID;
    }

    /**
     * Gets the volunteers assigned to the shelter.
     *
     * @return the list of volunteers
     */
    public ArrayList<Volunteer> getVolunteers() {
        return volunteers;
    }

    /**
     * Sets the volunteers assigned to the shelter.
     *
     * @param volunteers the new volunteer list
     */
    public void setVolunteers(ArrayList<Volunteer> volunteers) {
        this.volunteers = volunteers == null
                ? new ArrayList<>()
                : new ArrayList<>(volunteers);
    }

    /**
     * Adds a volunteer to the shelter.
     *
     * @param volunteer the volunteer to add
     */
    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
    }

    /**
     * Removes a volunteer from the shelter.
     *
     * @param volunteer the volunteer to remove
     */
    public void removeVolunteer(Volunteer volunteer) {
        volunteers.remove(volunteer);
    }

    /**
     * Gets the current shelter status.
     *
     * @return the shelter status
     */
    public ShelterStatus getStatus() {
        return status;
    }

    /**
     * Sets the current shelter status.
     *
     * @param status the new shelter status
     */
    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    /**
     * Gets the shelter manager.
     *
     * @return the shelter manager
     */
    public ShelterManager getManager() {
        return manager;
    }

    /**
     * Sets the shelter manager.
     *
     * @param manager the new shelter manager
     */
    public void setManager(ShelterManager manager) {
        this.manager = manager;
    }

    /**
     * Gets the UUID reference for the shelter manager.
     *
     * @return the manager UUID reference
     */
    public UUID getManagerUUIDReference() {
        return managerUUIDReference;
    }

    /**
     * Sets the UUID reference for the shelter manager.
     *
     * @param managerUUIDReference the manager UUID reference
     */
    public void setManagerUUIDReference(UUID managerUUIDReference) {
        this.managerUUIDReference = managerUUIDReference;
    }

    /**
     * Checks in a group of living beings when pet rules allow it.
     *
     * @param livingBeings the living beings to check in
     * @return true if every living being was checked in; otherwise false
     */
    public boolean checkIn(ArrayList<LivingBeing> livingBeings) {
        ArrayList<LivingBeing> occupantsToAdd = new ArrayList<>();

        for (LivingBeing livingBeing : livingBeings) {
            if (livingBeing.getType() == LivingBeingType.PET
                    && !isPetFriendly) {
                return false;
            }

            occupantsToAdd.add(livingBeing);
        }

        occupants.addAll(occupantsToAdd);
        Database.getInstance().write(Shelter.class);
        return true;
    }

    /**
     * Returns a readable summary of the shelter and its occupants.
     *
     * @return a formatted shelter summary
     */
    @Override
    public String toString() {
        String formattedOccupants = "";

        for (LivingBeing occupant : occupants) {
            formattedOccupants += "\t\t" + occupant + "\n";
        }

        return "Shelter '" + name + "'"
                + "\n\tStatus: " + status
                + "\n\tCapacity: " + getNumOccupants() + "/" + capacity
                + "\n\tLocation: " + location
                + "\n\tContact Info: " + contactInfo
                + (isPetFriendly ? "\n\tAccomdates Pets" : "")
                // + "\n\tOccupants: "
                // + (formattedOccupants.isEmpty()
                //         ? "None"
                //         : "\n" + formattedOccupants)
                + (hasMedicalStaff() ? "\n\tHas Medical Staff" : "");
    }

    /**
     * Gets the UUID reference for the shelter contact.
     *
     * @return the contact UUID reference
     */
    public UUID getContactUUIDReference() {
        return contactUUIDReference;
    }

    /**
     * Sets the UUID reference for the shelter contact.
     *
     * @param contactUUIDReference the contact UUID reference
     */
    public void setContactUUIDReference(UUID contactUUIDReference) {
        this.contactUUIDReference = contactUUIDReference;
    }

    /**
     * Checks whether any assigned volunteer has medical-care skills.
     *
     * @return true if medical staff are available; otherwise false
     */
    public boolean hasMedicalStaff() {
        for (Volunteer volunteer : volunteers) {
            for (ReliefType reliefSkill : volunteer.getReliefTypes()) {
                if (reliefSkill.equals(ReliefType.MEDICAL_CARE)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the shelter accommodates pets.
     *
     * @return true if pets are accepted; otherwise false
     */
    public boolean isPetFriendly() {
        return isPetFriendly;
    }

    /**
     * Sets whether the shelter accommodates pets.
     *
     * @param isPetFriendly true if pets are accepted
     */
    public void setPetFriendly(boolean isPetFriendly) {
        this.isPetFriendly = isPetFriendly;
    }
}