package com.pending.model.data.serializables;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

public class ReliefRequest extends Serializable {
    public static String file = JSONFile.REQUESTS.getFilename();
    private String description;
    private Location location;
    private ArrayList<ReliefType> types;
    private ReliefStatus status;
    private Contact contactInfo;
    // private ArrayList<LivingBeing> livingBeings;

    private UUID contactUUIDReference;
    private UUID locationUUIDReference;

    public ReliefRequest(String description, Location location,
        ReliefStatus status) {
        super();
        this.description = description;
        this.location = location;
        this.types = new ArrayList<>();
        this.status = status;
        this.contactInfo = new Contact();
        
        locationUUIDReference = UUID.randomUUID();
        contactUUIDReference = UUID.randomUUID();

        serializeBehavior = new SerializeJSONReliefRequest(this);
    }

    public ReliefRequest(UUID id, String description, Location location,
        ReliefStatus status) {
        this(description, location, status);
        this.id = id;
    }

    public ReliefRequest() {
        this("", new Location(), ReliefStatus.REQUESTED);
    }

    public ReliefStatus getStatus() {
        return status;
    }

    public void setStatus(ReliefStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UUID getContactUUIDReference() {
        return contactUUIDReference;
    }

    public void setContactUUIDReference(UUID contactUUIDReference) {
        this.contactUUIDReference = contactUUIDReference;
    }

    public ArrayList<ReliefType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<ReliefType> types) {
        this.types = types;
    }

    public String toString() {
        String reliefTypesString = types.get(0).toString();
        for (int i = 1; i < types.size(); ++i) {
            reliefTypesString += ", " + types.get(i);
        }

        return "Relief Request"
            + "\n\tDescription: " + description
            + "\n\tRelief Status:" + status
            + "\n\tRelief Needs: " + reliefTypesString
            + "\n\tLocation: " + location
            + "\n\tContact Info: " + contactInfo;
    }

    public Contact getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(Contact contactInfo) {
        this.contactInfo = contactInfo;
    }

}

