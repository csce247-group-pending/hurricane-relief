package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeJSONUser;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

/**
 * Represents an application user who can create and manage relief requests.
 * A user is a human living being with login credentials, a location, and
 * primary and emergency contact information.
 */
public class User extends LivingBeing {
    /** The JSON file used to store user data. */
    public static String file = JSONFile.USERS.getFilename();

    private String password;

    private UUID locationUUIDReference;
    private Location location;

    private UUID userContactUUIDReference;
    private UUID emergencyContactUUIDReference;
    private Contact userContact;
    private Contact emergencyContact;

    private ArrayList<UUID> userReliefRequestsUUIDReference;
    private final ArrayList<ReliefRequest> userReliefRequests;

    /**
     * Creates a user with account, location, and contact information.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param reliefNeeds the user's current relief needs
     * @param password the account password
     * @param location the user's location
     * @param userContact the user's primary contact information
     * @param emergencyContact the user's emergency contact information
     */
    public User(String firstName, String lastName,
            LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
            String password, Location location, Contact userContact,
            Contact emergencyContact) {

        super(firstName, lastName, dateOfBirth,
                LivingBeingType.HUMAN, reliefNeeds);

        this.password = password;
        this.location = location;
        this.userContact = userContact;
        this.emergencyContact = emergencyContact;
        this.userReliefRequests = new ArrayList<>();

        userContactUUIDReference = userContact.getId();
        emergencyContactUUIDReference = emergencyContact == null
                ? null
                : emergencyContact.getId();
        locationUUIDReference = location.getId();

        userReliefRequestsUUIDReference = new ArrayList<>();
        serializeBehavior = new SerializeJSONUser(this);
    }

    /**
     * Creates a user with a specified UUID.
     *
     * @param id the user UUID
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param reliefNeeds the user's current relief needs
     * @param password the account password
     * @param location the user's location
     * @param userContact the user's primary contact information
     * @param emergencyContact the user's emergency contact information
     */
    public User(UUID id, String firstName, String lastName,
            LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
            String password, Location location, Contact userContact,
            Contact emergencyContact) {

        this(firstName, lastName, dateOfBirth, reliefNeeds,
                password, location, userContact, emergencyContact);
        this.id = id;
    }

    /**
     * Creates an empty user with default values.
     */
    public User() {
        this("", "", LocalDate.now(), new ArrayList<>(),
                "", new Location(), new Contact(), new Contact());
    }

    /**
     * Gets the account password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the account password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's location.
     *
     * @return the user location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the user's location.
     *
     * @param location the new user location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the UUID reference for the user's location.
     *
     * @return the location UUID reference
     */
    public UUID getLocationUUIDReference() {
        return locationUUIDReference;
    }

    /**
     * Sets the UUID reference for the user's location.
     *
     * @param id the location UUID reference
     */
    public void setLocationUUIDReference(UUID id) {
        locationUUIDReference = id;
    }

    /**
     * Gets the user's primary contact information.
     *
     * @return the primary contact
     */
    public Contact getUserContact() {
        if (userContact == null) {
            System.out.println(getFullName() + "has null contact");
        }
        return userContact;
    }

    /**
     * Sets the user's primary contact information.
     *
     * @param userContact the new primary contact
     */
    public void setUserContact(Contact userContact) {
        this.userContact = userContact;
    }

    /**
     * Gets the user's emergency contact information.
     *
     * @return the emergency contact
     */
    public Contact getEmergencyContact() {
        return emergencyContact;
    }

    /**
     * Sets the user's emergency contact information.
     *
     * @param emergencyContact the new emergency contact
     */
    public void setEmergencyContact(Contact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    /**
     * Gets the relief requests associated with this user.
     *
     * @return the list of user relief requests
     */
    public ArrayList<ReliefRequest> getUserReliefRequests() {
        return userReliefRequests;
    }

    /**
     * Gets UUID references for this user's relief requests.
     *
     * @return the list of relief-request UUID references
     */
    public ArrayList<UUID> getUserReliefRequestsUUIDReference() {
        return userReliefRequestsUUIDReference;
    }

    /**
     * Adds a relief-request reference using the request object.
     *
     * @param reliefRequest the relief request to add
     * @return true if the UUID reference was added
     */
    public boolean addUserReliefRequest(ReliefRequest reliefRequest) {
        userReliefRequests.add(reliefRequest);
        return userReliefRequestsUUIDReference.add(reliefRequest.getId());
    }

    /**
     * Adds a relief-request reference using its UUID.
     *
     * @param id the relief-request UUID
     * @return true if the UUID reference was added
     */
    public boolean addUserReliefRequest(UUID id) {
        return userReliefRequestsUUIDReference.add(id);
    }

    /**
     * Gets the UUID reference for the primary contact.
     *
     * @return the primary-contact UUID reference
     */
    public UUID getUserContactUUIDReference() {
        return userContactUUIDReference;
    }

    /**
     * Sets the UUID reference for the primary contact.
     *
     * @param userContactUUIDReference the primary-contact UUID reference
     */
    public void setUserContactUUIDReference(
            UUID userContactUUIDReference) {

        this.userContactUUIDReference = userContactUUIDReference;
    }

    /**
     * Gets the UUID reference for the emergency contact.
     *
     * @return the emergency-contact UUID reference
     */
    public UUID getEmergencyContactUUIDReference() {
        return emergencyContactUUIDReference;
    }

    /**
     * Sets the UUID reference for the emergency contact.
     *
     * @param emergencyContactUUIDReference the emergency-contact UUID reference
     */
    public void setEmergencyContactUUIDReference(
            UUID emergencyContactUUIDReference) {

        this.emergencyContactUUIDReference =
                emergencyContactUUIDReference;
    }

    /**
     * Accepts a relief request.
     * This behavior is implemented by volunteer subclasses.
     *
     * @param reliefRequest the request to accept
     * @return false because regular users cannot accept requests
     */
    public boolean acceptReliefRequest(ReliefRequest reliefRequest) {
        return false;
    }

    /**
     * Closes a completed relief request.
     * This behavior is implemented by shelter-manager subclasses.
     *
     * @param reliefRequest the request to close
     * @return false because regular users cannot close requests
     */
    public boolean closeReliefRequest(ReliefRequest reliefRequest) {
        return false;
    }

    /**
     * Checks a group of living beings into a shelter.
     * This behavior is implemented by shelter-manager subclasses.
     *
     * @param livingBeings the people or pets to check in
     * @return false because regular users cannot check in groups
     */
    public boolean checkIntoShelter(
            ArrayList<LivingBeing> livingBeings) {

        return false;
    }

    /**
     * Gets the user's full name.
     *
     * @return the user's first and last name
     */
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    /**
     * Returns a readable summary of the user.
     *
     * @return a formatted user summary
     */
    @Override
    public String toString() {
        String reliefRequestsString = "";
        if(!userReliefRequests.isEmpty()) {
            reliefRequestsString = "\nRelief Requests:\n1. " 
                + userReliefRequests.get(0).toStringDetailed();
            for(int i = 1; i < userReliefRequests.size(); ++i) {
                reliefRequestsString += "\n" + (i+1) + ". " 
                + userReliefRequests.get(i).toStringDetailed();
            }
        }

        return "{User " + getFullName() + "} "
                + "\n\tAge: " + getAge()
                + "\n\tContact Info: " + getUserContact()
                + reliefRequestsString;
    }

    /**
     * Returns the LivingBeing version of this user's description.
     *
     * @return the parent class description
     */
    public String toStringLivingBeing() {
        return super.toString();
    }
}