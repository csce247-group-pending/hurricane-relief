package com.pending.model.data.serializables;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.enums.JSONFile;

public class User extends Serializable {
    public static String file = JSONFile.USERS.getFilename();
    private String firstName;
    private String lastName;
    private String password;

    private UUID locationUUIDReference;
    private Location location;

    private UUID userContactUUIDReference;
    private UUID emergencyContactUUIDReference;
    private Contact userContact;
    private Contact emergencyContact;


    private ArrayList<UUID> userReliefRequestsUUIDReference;
    private final ArrayList<ReliefRequest> userReliefRequests;

    public User(String firstName, String lastName, String password,
                Location location, Contact userContact, Contact emergencyContact) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.location = location;
        this.userContact = userContact;
        this.emergencyContact = emergencyContact;
        this.userReliefRequests = new ArrayList<>();

        userContactUUIDReference = UUID.randomUUID();
        emergencyContactUUIDReference = UUID.randomUUID();
        locationUUIDReference = UUID.randomUUID();

        userReliefRequestsUUIDReference = new ArrayList<>();

        serializeBehavior = new SerializeJSONUser(this);
    }

    public User(UUID id, String firstName, String lastName,
                String password, Location location, Contact userContact,
                Contact emergencyContact) {
        this(firstName, lastName, password, location, userContact, emergencyContact);
        this.id = id;
    }

    public User() {
        this("", "", "", new Location(), new Contact(), new Contact());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getLocationUUIDReference() {
        return locationUUIDReference;
    }

    public void setLocationUUIDReference(UUID id) {
        locationUUIDReference = id;
    }

    public Contact getUserContact() {
        return userContact;
    }

    public void setUserContact(Contact userContact) {
        this.userContact = userContact;
    }

    public Contact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(Contact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public ArrayList<ReliefRequest> getUserReliefRequests() {
        return userReliefRequests;
    }

    public ArrayList<UUID> getUserReliefRequestsUUIDReference() {
        return userReliefRequestsUUIDReference;
    }

    public boolean addUserReliefRequest(ReliefRequest reliefRequest) {
        return userReliefRequestsUUIDReference.add(reliefRequest.getId());
    }

    public boolean addUserReliefRequest(UUID id) {
        return userReliefRequestsUUIDReference.add(id);
    }

    public UUID getUserContactUUIDReference() {
        return userContactUUIDReference;
    }

    public void setUserContactUUIDReference(UUID userContactUUIDReference) {
        this.userContactUUIDReference = userContactUUIDReference;
    }

    public UUID getEmergencyContactUUIDReference() {
        return emergencyContactUUIDReference;
    }

    public void setEmergencyContactUUIDReference(UUID emergencyContactUUIDReference) {
        this.emergencyContactUUIDReference = emergencyContactUUIDReference;
    }
}