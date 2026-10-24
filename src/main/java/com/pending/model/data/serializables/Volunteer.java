package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.SkillSort;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefType;


public class Volunteer extends User {
    public static final String file = JSONFile.VOLUNTEERS.getFilename();
    private boolean isAvailable;
    private ArrayList<ReliefType> reliefTypes;
    private ReliefRequest task;
    private SkillSort sorter;

    public Volunteer(UUID id, String firstName, String lastName, LocalDate dateOfBirth,
                     ArrayList<ReliefType> reliefNeeds, String password,
                     Location location, Contact userContact, Contact emergencyContact,
                     boolean isAvailable, ArrayList<ReliefType> reliefTypes, ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, reliefNeeds, password,
            location, userContact, emergencyContact);
        
        this.isAvailable = isAvailable;
        this.reliefTypes = reliefTypes;
        this.task = task;
        sorter = new SkillSort(this);
    }

    public Volunteer() {
        super();
        this.isAvailable = true;
        this.reliefTypes = new ArrayList<>();
        this.task = new ReliefRequest();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<ReliefType> getReliefTypes() {
        return reliefTypes;
    }

    public void addReliefType(ReliefType reliefType) {
        reliefTypes.add(reliefType);
    }

    public void removeReliefType(ReliefType reliefType) {
        reliefTypes.remove(reliefType);
    }

    public ReliefRequest getTask() {
        return task;
    }

    public void setTask(ReliefRequest task) {
        this.task = task;
    }

    public SkillSort getSorter() {
        return sorter;
    }

    public void closeReliefRequest(ReliefRequest reliefRequest) {
        System.out.println("Permission denied");
        System.out.printf("Reason: you're a chud and your mom's a chud");
    }

    /**
     * Takes task and exports it to a txt file that has
     * directions to the relief request and to the shelter
     * 
     * @return boolean of whether the file was successfully made
     */
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