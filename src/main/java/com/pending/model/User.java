package com.pending.model;

import java.util.ArrayList;
import java.util.UUID;

import javafx.scene.layout.Priority;

public class User {
    private UUID id;
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

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {

    }

    public Contact getUserContact() {
        return userContact;
    }   

    public void setUserContact(Contact userContact) {

    }

    public Contact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(Contact emergencyContact) {

    }

    public ArrayList<ReliefRequest> getUserReliefRequests() {
        return userReliefRequests;
    }

    public void setUserReliefRequests(ArrayList<ReliefRequest> userReliefRequests) {

    }

}