package com.pending.model;

import java.util.ArrayList;

public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;

    private Application() {

    }

    public Application getIstance() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    public User createAccountPopup() {
        // prompt user for the account information
        // return requestCreateAccount(firstName, lastName, password, location, userContact, emergencyContact);
        return null;
    }

    private User requestCreateAccount(String firstName, String lastName, String password,
        Location location, Contact userContact, Contact emergencyContact
    ) {
        return null;
    }

    public boolean attemptLogin(String email, String password) {
        return false;
    }

    public void logout() {

    }

    public void createReliefRequest() {

    }

    public ArrayList<Shelter> findNearbyShelters(Location location) {
        return null;
    }

    public ArrayList<Shelter> findNearbyResource(Location location, Resource resource) {
        return null;
    }

    public ArrayList<ReliefRequest> getRequests() {
        return null;
    }

    public ArrayList<Hurricane> getHurricaneData() {
        return null;
    }

    public ArrayList<Contact> getContacts() {
        return null;
    }

    public void run() {

    }

    private Location getLocation() {
        return location;
    }

    private void updateLocation(Location location) {
        this.location = location;
    }
}

