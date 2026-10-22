package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.Skill;

public class Volunteer extends User {
    private boolean isAvailable;
    private ArrayList<Skill> skills;
    private ReliefRequest task;

    public Volunteer(UUID id, String firstName, String lastName, LocalDate dateOfBirth, String password,
                     Location location, Contact userContact, Contact emergencyContact,
                     boolean isAvailable, ArrayList<Skill> skills, ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, password,
            location, userContact, emergencyContact);
        
        this.isAvailable = isAvailable;
        this.skills = skills;
        this.task = task;
    }

    public Volunteer() {
        super();
        this.isAvailable = true;
        this.skills = new ArrayList<>();
        this.task = new ReliefRequest();
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

    public ReliefRequest getTask() {
        return task;
    }

    public void setTask(ReliefRequest task) {
        this.task = task;
    }
}