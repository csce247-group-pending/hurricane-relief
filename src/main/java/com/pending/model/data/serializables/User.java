package com.pending.model.data.serializables;

import com.pending.model.enums.JSONFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class User extends Serializable {
    public static String file = JSONFile.USERS.getFilename();
    private String firstName;
    private String lastName;
    private String password;
    private Location location;

    private UUID userContactUUIDReference;
    private UUID emergencyContactUUIDReference;
    private Contact userContact;
    private Contact emergencyContact;


    private ArrayList<UUID> userReliefRequestsByUUID;
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

        serializeBehavior = new SerializeJSONUser(this);
    }

    public User(UUID id, String firstName, String lastName,
                String password, Location location, Contact userContact,
                Contact emergencyContact) {
        this(firstName, lastName, password, location, userContact, emergencyContact);
        this.id = id;
    }

    public User() {
        this("", "", "", null, null, null);
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

    public ArrayList<UUID> getUserReliefRequestsByUUID() {
        return userReliefRequestsByUUID;
    }

    public boolean addUserReliefRequest(ReliefRequest reliefRequest) {
        return userReliefRequestsByUUID.add(reliefRequest.getId());
    }

    public boolean addUserReliefRequest(UUID id) {
        return userReliefRequestsByUUID.add(id);
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

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("password", password);
        jsonObject.put("location", location);
        jsonObject.put("userContact", userContact.getId());
        jsonObject.put("emergencyContact", emergencyContact.getId());

        JSONArray userReliefRequests = new JSONArray();
        userReliefRequests.addAll(this.userReliefRequestsByUUID);

        jsonObject.put("userReliefRequests", userReliefRequests);

        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        id = UUID.fromString((String) jsonObject.get("id"));
        firstName = (String) jsonObject.get("firstName");
        lastName = (String) jsonObject.get("lastName");
        password = (String) jsonObject.get("password");

        JSONObject locationJsonObject = (JSONObject) jsonObject.get("location");
        location.deserialize(locationJsonObject);
    }

}