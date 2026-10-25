package com.pending.model.data.serializables;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.pending.model.data.serializables.behaviors.SerializeJSONReliefRequest;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Represents a request for disaster-relief assistance.
 * A request includes a description, location, needed relief types,
 * contact information, affected living beings, and an optional volunteer.
 */
public class ReliefRequest extends Serializable {
    /** The JSON file used to store relief-request data. */
    public static String file = JSONFile.REQUESTS.getFilename();

    private String description;
    private Location location;
    private ArrayList<ReliefType> types;
    private ReliefStatus status;
    private Contact contactInfo;
    private ArrayList<LivingBeing> livingBeings;

    private UUID contactUUIDReference;
    private UUID locationUUIDReference;

    private Volunteer assignedTo;
    private UUID assignedToUUIDReference;

    /**
     * Creates a relief request with a generated UUID.
     *
     * @param description a description of the requested assistance
     * @param location the location needing assistance
     * @param status the current request status
     * @param livingBeings the people or pets affected by the request
     * @param reliefTypes the needed types of relief
     */
    public ReliefRequest(String description, Location location,
            ReliefStatus status, ArrayList<LivingBeing> livingBeings,
            ArrayList<ReliefType> reliefTypes) {

        super();
        this.description = description;
        this.location = location;
        this.types = reliefTypes;
        this.status = status;
        this.contactInfo = new Contact();
        this.livingBeings = livingBeings;
        this.assignedTo = null;
        this.assignedToUUIDReference = null;

        locationUUIDReference = UUID.randomUUID();
        contactUUIDReference = UUID.randomUUID();

        serializeBehavior = new SerializeJSONReliefRequest(this);
    }

    /**
     * Creates a relief request with a specified UUID.
     *
     * @param id the relief-request UUID
     * @param description a description of the requested assistance
     * @param location the location needing assistance
     * @param status the current request status
     * @param livingBeings the people or pets affected by the request
     * @param reliefTypes the needed types of relief
     */
    public ReliefRequest(UUID id, String description, Location location,
            ReliefStatus status, ArrayList<LivingBeing> livingBeings,
            ArrayList<ReliefType> reliefTypes) {

        this(description, location, status, livingBeings, reliefTypes);
        this.id = id;
    }

    /**
     * Creates an empty relief request with requested status.
     */
    public ReliefRequest() {
        this("", new Location(), ReliefStatus.REQUESTED,
                new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Gets the request status.
     *
     * @return the current relief-request status
     */
    public ReliefStatus getStatus() {
        return status;
    }

    /**
     * Sets the request status.
     *
     * @param status the new relief-request status
     */
    public void setStatus(ReliefStatus status) {
        this.status = status;
    }

    /**
     * Gets the request description.
     *
     * @return the request description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the request description.
     *
     * @param description the new request description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the request location.
     *
     * @return the location needing assistance
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the request location.
     *
     * @param location the new request location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the UUID reference for the request location.
     *
     * @return the location UUID reference
     */
    public UUID getLocationUUIDReference() {
        return locationUUIDReference;
    }

    /**
     * Sets the UUID reference for the request location.
     *
     * @param locationUUIDReference the location UUID reference
     */
    public void setLocationUUIDReference(UUID locationUUIDReference) {
        this.locationUUIDReference = locationUUIDReference;
    }

    /**
     * Gets the UUID reference for the request contact.
     *
     * @return the contact UUID reference
     */
    public UUID getContactUUIDReference() {
        return contactUUIDReference;
    }

    /**
     * Sets the UUID reference for the request contact.
     *
     * @param contactUUIDReference the contact UUID reference
     */
    public void setContactUUIDReference(UUID contactUUIDReference) {
        this.contactUUIDReference = contactUUIDReference;
    }

    /**
     * Gets the types of relief needed.
     *
     * @return the list of needed relief types
     */
    public ArrayList<ReliefType> getTypes() {
        return types;
    }

    /**
     * Sets the types of relief needed.
     *
     * @param types the new list of relief types
     */
    public void setTypes(ArrayList<ReliefType> types) {
        this.types = types;
    }

    private void setTypesFromLivingBeings() {
        Set<ReliefType> uniqueTypes = new LinkedHashSet<>();
        for(LivingBeing being: livingBeings) {
            uniqueTypes.addAll(being.getReliefNeeds());
        }

        types = new ArrayList<ReliefType>(uniqueTypes);
    }

    /**
     * Returns a readable summary of this relief request.
     *
     * @return a formatted relief-request summary
     */
    @Override
    public String toString() {
        String reliefTypesString = types.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        reliefTypesString = reliefTypesString.isEmpty()
                ? "N/A"
                : reliefTypesString;

        return "{Relief Request}"
                + "\n\tDescription: " + description
                + "\n\tRelief Status: " + status
                + "\n\tRelief Needs: " + reliefTypesString
                + "\n\tLocation: " + location
                + "\n\tMembers" + getPartySize() + ")";
    }

    public String toStringDetailed() {
        String reliefTypesString = types.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        reliefTypesString = reliefTypesString.isEmpty()
                ? "N/A"
                : reliefTypesString;

        String formattedBeings = "";

        for (LivingBeing livingBeing : livingBeings) {
            if (livingBeing instanceof User user) {
                formattedBeings += "\t\t" + user.toStringLivingBeing() + "\n";
            } else {
                formattedBeings += "\t\t" + livingBeing.toString() + "\n";
            }
        }

        return "{Relief Request Detailed}"
                + "\n\tDescription: " + description
                + "\n\tRelief Status: " + status
                + "\n\tAssigned To: " + (assignedTo != null && status != ReliefStatus.REQUESTED ? 
                assignedTo.getFullName() : "None")
                + "\n\tRelief Needs: " + reliefTypesString
                + "\n\tLocation: " + location
                + "\n\tContact Info: " + contactInfo
                + "\n\tMembers(" + getPartySize() + "): "
                + (formattedBeings.isEmpty()
                        ? "None"
                        : "\n" + formattedBeings);
    }

    /**
     * Gets contact information for this request.
     *
     * @return the request contact information
     */
    public Contact getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets contact information for this request.
     *
     * @param contactInfo the new request contact information
     */
    public void setContactInfo(Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the people and pets affected by this request.
     *
     * @return the list of affected living beings
     */
    public ArrayList<LivingBeing> getLivingBeings() {
        return livingBeings;
    }

    /**
     * Adds an affected living being to this request.
     *
     * @param livingBeing the living being to add
     * @return true if the living being was added
     */
    public boolean addLivingBeing(LivingBeing livingBeing) {
        return livingBeings.add(livingBeing);
    }

    /**
     * Gets the volunteer assigned to this request.
     *
     * @return the assigned volunteer, or null when unassigned
     */
    public Volunteer getAssignedTo() {
        return assignedTo;
    }

    /**
     * Sets the volunteer assigned to this request.
     *
     * @param assignedTo the volunteer to assign
     */
    public void setAssignedTo(Volunteer assignedTo) {
        this.assignedTo = assignedTo;
    }

    /**
     * Gets the UUID reference for the assigned volunteer.
     *
     * @return the volunteer UUID reference
     */
    public UUID getAssignedToUUIDReference() {
        return assignedToUUIDReference;
    }

    /**
     * Sets the UUID reference for the assigned volunteer.
     *
     * @param assignedToUUIDReference the volunteer UUID reference
     */
    public void setAssignedToUUIDReference(
            UUID assignedToUUIDReference) {

        this.assignedToUUIDReference = assignedToUUIDReference;
    }

    public boolean hasPets() {
        for(LivingBeing being: livingBeings) {
            if(being.getType() == LivingBeingType.PET) {
                return true;
            }
        }
        return false;
    }

    public int getPartySize() {
        return livingBeings.size();
    }
}