package com.pending.model;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class User extends Serializable {
    private String firstName;
    private String lastName;
    private String password;
    private Location location;
    private Contact userContact;
    private Contact emergencyContact;
    private ArrayList<ReliefRequest> userReliefRequests;

    public User(UUID id, String firstName, String lastName,
        String password, Location location, Contact userContact,
        Contact emergencyContact) {

    }

    public User(String firstName, String lastName, String password,
        Location location, Contact userContact, Contact emergencyContact) {
        super();
    }

    public void requestRelief(Priority priority, String description,
        Location location, ArrayList<Resource> neededResources) {

    }

    public void viewReliefRequest(ReliefRequest reliefRequest) {

    }

    public void viewHurricaneStatus() {

    }
    public void cancelReliefRequest(ReliefRequest reliefRequest) {

    }
    public void acknowledgeAlert() {

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
        jsonObject.put("userReliefRequests", userReliefRequests.getId());

        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        id = UUID.fromString((String) jsonObject.get("id"));
        firstName = (String) jsonObject.get("firstName");
        lastName = (String) jsonObject.get("lastName");
        password = (String) jsonObject.get("password");

        JSONObject locationJsonObject = (JSONObject) jsonObject.get("location");
        location.deserialize(locationJsonObject);

        userContact =

    }

}