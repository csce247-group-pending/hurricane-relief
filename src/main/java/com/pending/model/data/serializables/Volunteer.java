package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.SkillSort;
import com.pending.model.enums.Skill;


public class Volunteer extends User {
    private boolean isAvailable;
    private ArrayList<Skill> skills;
    private ReliefRequest task;
    private SkillSort sorter;

    public Volunteer(UUID id, String firstName, String lastName, LocalDate dateOfBirth, 
        ArrayList<ReliefType> reliefNeeds, String password,
                     Location location, Contact userContact, Contact emergencyContact,
                     boolean isAvailable, ArrayList<Skill> skills, ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, reliefNeeds, password,
            location, userContact, emergencyContact);
        
        this.isAvailable = isAvailable;
        this.skills = skills;
        this.task = task;
        sorter = new SkillSort(this);
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

    public SkillSort getSorter() {
        return sorter;
    }
}