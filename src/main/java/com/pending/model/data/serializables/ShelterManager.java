package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.behaviors.SerializeJSONShelterManager;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;
import com.pending.model.enums.Skill;
import org.json.simple.JSONObject;

public class ShelterManager extends Volunteer {
    public static final String file = JSONFile.SHELTER_MANAGERS.getFilename();
    private Shelter shelter;
    private UUID shelterUUIDReference;

    public ShelterManager(UUID id, String firstName, String lastName,
                          LocalDate dateOfBirth, String password, Location location,
                          Contact userContact, Contact emergencyContact,
                          boolean isAvailable, ArrayList<ReliefType> reliefTypes, ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, new ArrayList<>(),
            password, location, userContact, emergencyContact, 
            isAvailable, reliefTypes, task);
        serializeBehavior = new SerializeJSONShelterManager<ShelterManager>(this);
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
        serializeBehavior = new SerializeJSONShelterManager<ShelterManager>(this);
    }

    public ShelterManager() {
        super();
        serializeBehavior = new SerializeJSONShelterManager<ShelterManager>(this);
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

    @Override
    public boolean checkIntoShelter(ArrayList<LivingBeing> livingBeings) {
        return shelter.checkIn(livingBeings);
    }

    @Override
    public boolean closeReliefRequest(ReliefRequest reliefRequest) {
        reliefRequest.setStatus(ReliefStatus.COMPLETED);
        Database.getInstance().write(ReliefRequest.class);
        return true;
    }
}
