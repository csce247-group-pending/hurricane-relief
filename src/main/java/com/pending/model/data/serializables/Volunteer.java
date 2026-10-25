package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.SkillSort;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Represents a user who can volunteer to complete relief requests.
 * A volunteer has availability, a list of relief skills, and an optional
 * currently assigned task.
 */
public class Volunteer extends User {
    /** The JSON file used to store volunteer data. */
    public static final String file = JSONFile.VOLUNTEERS.getFilename();

    private boolean isAvailable;
    private ArrayList<ReliefType> reliefTypes;
    private ReliefRequest task;
    private SkillSort sorter;

    /**
     * Creates a volunteer with account, skill, and task information.
     *
     * @param firstName the volunteer's first name
     * @param lastName the volunteer's last name
     * @param dateOfBirth the volunteer's date of birth
     * @param reliefNeeds the volunteer's personal relief needs
     * @param password the volunteer's account password
     * @param location the volunteer's location
     * @param userContact the volunteer's primary contact information
     * @param emergencyContact the volunteer's emergency contact information
     * @param isAvailable whether the volunteer is currently available
     * @param reliefTypes the assistance types the volunteer can provide
     * @param task the request currently assigned to the volunteer
     */
    public Volunteer(String firstName, String lastName,
            LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
            String password, Location location, Contact userContact,
            Contact emergencyContact, boolean isAvailable,
            ArrayList<ReliefType> reliefTypes, ReliefRequest task) {

        super(firstName, lastName, dateOfBirth, reliefNeeds, password,
                location, userContact, emergencyContact);

        this.isAvailable = isAvailable;
        this.reliefTypes = reliefTypes;
        this.task = task;
        sorter = new SkillSort(this);
    }

    /**
     * Creates a volunteer with a specified UUID.
     *
     * @param id the volunteer UUID
     * @param firstName the volunteer's first name
     * @param lastName the volunteer's last name
     * @param dateOfBirth the volunteer's date of birth
     * @param reliefNeeds the volunteer's personal relief needs
     * @param password the volunteer's account password
     * @param location the volunteer's location
     * @param userContact the volunteer's primary contact information
     * @param emergencyContact the volunteer's emergency contact information
     * @param isAvailable whether the volunteer is currently available
     * @param reliefTypes the assistance types the volunteer can provide
     * @param task the request currently assigned to the volunteer
     */
    public Volunteer(UUID id, String firstName, String lastName,
            LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
            String password, Location location, Contact userContact,
            Contact emergencyContact, boolean isAvailable,
            ArrayList<ReliefType> reliefTypes, ReliefRequest task) {

        super(id, firstName, lastName, dateOfBirth, reliefNeeds, password,
                location, userContact, emergencyContact);

        this.isAvailable = isAvailable;
        this.reliefTypes = reliefTypes;
        this.task = task;
        sorter = new SkillSort(this);
    }

    /**
     * Creates an available volunteer with no skills or assigned task.
     */
    public Volunteer() {
        super();
        this.isAvailable = true;
        this.reliefTypes = new ArrayList<>();
        this.task = null;
        sorter = new SkillSort(this);
    }

    /**
     * Checks whether the volunteer is currently available.
     *
     * @return true if the volunteer is available; otherwise false
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the volunteer's availability.
     *
     * @param isAvailable true if the volunteer is available
     */
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Gets the relief skills the volunteer can provide.
     *
     * @return the list of volunteer relief skills
     */
    public ArrayList<ReliefType> getReliefTypes() {
        return reliefTypes;
    }

    /**
     * Adds a relief skill for this volunteer.
     *
     * @param reliefType the relief skill to add
     */
    public void addReliefType(ReliefType reliefType) {
        reliefTypes.add(reliefType);
    }

    /**
     * Removes a relief skill from this volunteer.
     *
     * @param reliefType the relief skill to remove
     */
    public void removeReliefType(ReliefType reliefType) {
        reliefTypes.remove(reliefType);
    }

    /**
     * Gets the relief request currently assigned to this volunteer.
     *
     * @return the assigned request, or null when unassigned
     */
    public ReliefRequest getTask() {
        return task;
    }

    /**
     * Sets the relief request assigned to this volunteer.
     *
     * @param task the request to assign
     */
    public void setTask(ReliefRequest task) {
        this.task = task;
    }

    /**
     * Gets the skill sorter associated with this volunteer.
     *
     * @return the volunteer skill sorter
     */
    public SkillSort getSorter() {
        return sorter;
    }

    /**
     * Accepts and assigns a relief request to this volunteer.
     *
     * @param reliefRequest the request to accept
     * @return true after the request is assigned
     */
    @Override
    public boolean acceptReliefRequest(ReliefRequest reliefRequest) {
        task = reliefRequest;
        reliefRequest.setAssignedTo(this);
        reliefRequest.setStatus(ReliefStatus.ASSIGNED);
        return true;
    }

    public boolean openReliefRequest(ReliefRequest reliefRequest) {
        task = reliefRequest;
        return true;
    }

    public String exportTripInformation() {
        String toWrite = "Relief Request Trip Information:";
        toWrite += "\n" + task.toString();
        // add directions to Carter home
        toWrite += "\nDirections to Task from Current Location:";


        // add directions to shelter + shelter informations
        toWrite += "\nShelter Information:";

        toWrite += "\nDirections to Shelter from Task:";

        
        return toWrite;
    }
}