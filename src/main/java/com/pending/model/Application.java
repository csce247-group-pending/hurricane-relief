package com.pending.model;

import java.util.ArrayList;

public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;

    private Application() {
        database = Database.getInstance();
        currentUser = null;
        location = null;
    }

    public static Application getInstance() {
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

    //? Should this be switched from return User to return boolean?
    public boolean attemptLogin(String email, String password) {
        currentUser = database.attemptLogin(email, password);
        return currentUser != null;
    }

    public void logout() {

    }

    public String getUserFullName() {
        return currentUser.getFirstName() + " " + currentUser.getLastName();
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

