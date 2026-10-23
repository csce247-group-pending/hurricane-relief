package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.ReliefType;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.Skill;

public class ShelterManager extends Volunteer {
    private Shelter shelter;
    private UUID shelterUUIDReference;

    public ShelterManager(UUID id, String firstName, String lastName, 
        LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds, String password, Location location, 
        Contact userContact, Contact emergencyContact, 
        boolean isAvailable, ArrayList<Skill> skills, ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, reliefNeeds,
            password, location, userContact, emergencyContact, 
            isAvailable, reliefTypes, task);
    }

    public ShelterManager(Volunteer volunteer) {
        super(
                volunteer.getId(),
                volunteer.getFirstName(),
                volunteer.getLastName(),
                volunteer.getDateOfBirth(),
                volunteer.getReliefNeeds(),
                volunteer.getPassword(),
                volunteer.getLocation(),
                volunteer.getUserContact(),
                volunteer.getEmergencyContact(),
                volunteer.isAvailable(),
                new ArrayList<>(volunteer.getReliefTypes()),
                volunteer.getTask()
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
