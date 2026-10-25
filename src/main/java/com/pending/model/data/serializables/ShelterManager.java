package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeJSONShelterManager;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Represents a volunteer who manages a shelter.
 * A shelter manager can check people into their assigned shelter
 * and mark relief requests as completed.
 */
public class ShelterManager extends Volunteer {
    /** The JSON file used to store shelter manager records. */
    public static final String file = JSONFile.SHELTER_MANAGERS.getFilename();

    /** The shelter managed by this shelter manager. */
    private Shelter shelter;

    /** UUID reference used to reconnect the managed shelter after loading data. */
    private UUID shelterUUIDReference;

    /**
     * Creates a shelter manager with the provided account and volunteer details.
     *
     * @param id the unique identifier for the manager
     * @param firstName the manager's first name
     * @param lastName the manager's last name
     * @param dateOfBirth the manager's date of birth
     * @param password the account password
     * @param location the manager's location
     * @param userContact the manager's primary contact information
     * @param emergencyContact the manager's emergency contact information
     * @param isAvailable whether the manager is available for assignments
     * @param reliefTypes the relief skills the manager has
     * @param task the relief request currently assigned to the manager
     */
    public ShelterManager(UUID id, String firstName, String lastName,
            LocalDate dateOfBirth, String password, Location location,
            Contact userContact, Contact emergencyContact,
            boolean isAvailable, ArrayList<ReliefType> reliefTypes,
            ReliefRequest task) {
        super(id, firstName, lastName, dateOfBirth, new ArrayList<>(),
                password, location, userContact, emergencyContact,
                isAvailable, reliefTypes, task);

        serializeBehavior = new SerializeJSONShelterManager<ShelterManager>(this);
    }

    /**
     * Creates a shelter manager from an existing volunteer.
     *
     * @param volunteer the volunteer being promoted to shelter manager
     */
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

    /**
     * Creates an empty shelter manager for deserialization.
     */
    public ShelterManager() {
        super();
        serializeBehavior = new SerializeJSONShelterManager<ShelterManager>(this);
    }

    /**
     * Gets the shelter managed by this manager.
     *
     * @return the managed shelter
     */
    public Shelter getShelter() {
        return shelter;
    }

    /**
     * Sets the shelter managed by this manager.
     *
     * @param shelter the shelter to manage
     */
    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    /**
     * Gets the UUID reference for the managed shelter.
     *
     * @return the shelter UUID reference
     */
    public UUID getShelterUUIDReference() {
        return shelterUUIDReference;
    }

    /**
     * Sets the UUID reference for the managed shelter.
     *
     * @param shelterUUIDReference the shelter UUID reference
     */
    public void setShelterUUIDReference(UUID shelterUUIDReference) {
        this.shelterUUIDReference = shelterUUIDReference;
    }

    /**
     * Marks a relief request as completed and saves the updated request data.
     *
     * @param reliefRequest the request to complete
     * @return true after the request is marked completed
     */
    public boolean closeReliefRequest(ReliefRequest reliefRequest) {
        reliefRequest.setStatus(ReliefStatus.COMPLETED);
        Volunteer assigned = reliefRequest.getAssignedTo();
        assigned.setAvailable(true);
        assigned.setTask(null);
        return true;
    }
}