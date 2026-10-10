package com.pending.model.data.serializables;

import com.pending.model.Skill;

import java.util.ArrayList;
import java.util.UUID;

public class Volunteer extends User {
    private boolean isAvailable;
    private ArrayList<Skill> skills;
    private ArrayList<ReliefRequest> tasks;

    public Volunteer(UUID id, String firstName, String lastName, String password,
                     Location location, Contact userContact, Contact emergencyContact,
                     boolean isAvailable, ArrayList<Skill> skills, ArrayList<ReliefRequest> tasks) {
        super(id, firstName, lastName, password,
            location, userContact, emergencyContact);
    }

    public void startRequest(ReliefRequest reliefRequest) {

    }

    public void completeRequest(ReliefRequest reliefRequest) {

    }

    public void viewRequests(ArrayList<ReliefRequest> tasks) {
        
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }

    public ArrayList<ReliefRequest> getTasks() {
        return tasks;
    }

    public void addReliefRequest(ReliefRequest reliefRequest) {
        tasks.add(reliefRequest);
    }

    public void removeReliefRequest(ReliefRequest reliefRequest) {
        tasks.remove(reliefRequest);
    }
}