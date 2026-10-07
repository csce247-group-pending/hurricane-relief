package com.pending.model;

import org.json.simple.JSONArray;
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
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.location = location;
        this.userContact = userContact;
        this.emergencyContact = emergencyContact;
        this.userReliefRequests = new ArrayList<ReliefRequest>();
    }

    public User(String firstName, String lastName, String password,
            Location location, Contact userContact, Contact emergencyContact) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.location = location;
        this.userContact = userContact;
        this.emergencyContact = emergencyContact;
        this.userReliefRequests = new ArrayList<ReliefRequest>();
    }

    public void requestRelief(Priority priority, String description,
            Location location, ArrayList<Resource> neededResources) {
        ReliefRequest reliefRequest = new ReliefRequest(
                priority, description, location, neededResources);

        userReliefRequests.add(reliefRequest);
        System.out.println("Relief request submitted.");
    }

    public void viewReliefRequest(ReliefRequest reliefRequest) {
        if (userReliefRequests.contains(reliefRequest)) {
            System.out.println("Priority: " + reliefRequest.getPriority());
            System.out.println("Status: " + reliefRequest.getStatus());
            System.out.println("Description: " + reliefRequest.getDescription());
        } else {
            System.out.println("Relief request not found.");
        }
    }

    public void viewHurricaneStatus() {
        System.out.println("Hurricane status is available through the application.");
    }

    public void cancelReliefRequest(ReliefRequest reliefRequest) {
        if (userReliefRequests.remove(reliefRequest)) {
            System.out.println("Relief request cancelled.");
        } else {
            System.out.println("Relief request not found.");
        }
    }

    public void acknowledgeAlert() {
        System.out.println("Alert acknowledged.");
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id.toString());
        jsonObject.put("firstName", firstName);
        jsonObject.put("lastName", lastName);
        jsonObject.put("password", password);
        jsonObject.put("location", location == null ? null : location.serialize());

        if (userContact != null) {
            jsonObject.put("userContact", userContact.getId().toString());
        }

        if (emergencyContact != null) {
            jsonObject.put("emergencyContact", emergencyContact.getId().toString());
        }

        JSONArray reliefRequestIds = new JSONArray();

        for (ReliefRequest reliefRequest : userReliefRequests) {
            if (reliefRequest.getId() != null) {
                reliefRequestIds.add(reliefRequest.getId().toString());
            }
        }

        jsonObject.put("userReliefRequests", reliefRequestIds);

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        id = UUID.fromString((String) jsonObject.get("id"));
        firstName = (String) jsonObject.get("firstName");
        lastName = (String) jsonObject.get("lastName");
        password = (String) jsonObject.get("password");

        JSONObject locationJsonObject =
                (JSONObject) jsonObject.get("location");

        if (locationJsonObject != null) {
            location = new Location("", "", "", "", "");
            location.deserialize(locationJsonObject);
        }

        userReliefRequests = new ArrayList<ReliefRequest>();
    }
}