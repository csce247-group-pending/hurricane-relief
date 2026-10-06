package com.pending.model;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class ReliefRequest extends Serializable {

    private UUID id;
    private Priority priority;
    private ReliefStatus status;
    private String description;
    private Location location;
    private LocalDateTime submittedAt;
    private ArrayList<Resource> neededResources;
    private File attachedPhoto;
    private ReliefType type;

    public ReliefRequest(Priority priority, String description, Location location,
        ArrayList<Resource> neededResources) {

    }

    public ReliefRequest(UUID id, Priority priority, String description, Location location,
        ArrayList<Resource> neededResources) {
 
    }

    public UUID getId() {
        return id;
    }

    public ReliefStatus getStatus() {
        return status;
    }

    public void setStatus(ReliefStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ArrayList<Resource> getNeededResources() {
        return neededResources;
    }

    public void addNeededResources(Resource addResource) {
        neededResources.add(addResource);
    }

    public File getAttachedPhoto() {
        return attachedPhoto;
    }

    public void setAttachedPhoto(File attachedPhoto) {
        this.attachedPhoto = attachedPhoto;
    }

    public ReliefType getType() {
        return type;
    }

    public void setType(ReliefType type) {
        this.type = type;
    }

}

