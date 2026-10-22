package com.pending.model.data.serializables;

import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeJSONReliefRequest;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

public class ReliefRequest extends Serializable {
    public static String file = JSONFile.REQUESTS.getFilename();
    private ReliefStatus status;
    private String description;
    private Location location;
    private UUID locationUUIDReference;
    private ReliefType type;

    public ReliefRequest(String description, Location location) {
        super();
        this.description = description;
        this.location = location;
        serializeBehavior = new SerializeJSONReliefRequest(this);
    }

    public ReliefRequest(UUID id, String description, Location location) {
        this(description, location);
        this.id = id;
    }

    public ReliefRequest() {
        this("", new Location());
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

    public ReliefType getType() {
        return type;
    }

    public void setType(ReliefType type) {
        this.type = type;
    }
}

