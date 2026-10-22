package com.pending.model.data.serializables;

import com.pending.model.enums.Skill;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class ShelterManager extends Volunteer {
    private Shelter shelter;
    private UUID shelterUUIDReference;

    public ShelterManager(UUID id, String firstName, String lastName, LocalDate dateOfBirth, String password, Location location, Contact userContact, Contact emergencyContact, boolean isAvailable, ArrayList<Skill> skills, ArrayList<ReliefRequest> tasks) {
        super(id, firstName, lastName, dateOfBirth, password, location, userContact, emergencyContact, isAvailable, skills, tasks);
    }

    public ShelterManager(Volunteer volunteer) {
        super(
                volunteer.getId(),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getDateOfBirth(),
                volunteer.getPassword(),
                volunteer.getLocation(),
                volunteer.getUserContact(),
                volunteer.getEmergencyContact(),
                volunteer.isAvailable(),
                new ArrayList<>(volunteer.getSkills()),
                new ArrayList<>(volunteer.getTasks())
        );
    }

    public ShelterManager() {
        super();
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    public UUID getShelterUUIDReference() {
        return shelterUUIDReference;
    }

    public void setShelterUUIDReference(UUID shelterUUIDReference) {
        this.shelterUUIDReference = shelterUUIDReference;
    }
}
