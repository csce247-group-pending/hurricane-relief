package com.pending.model;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.*;
import com.pending.model.data.DataManager;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ShelterStatus;
import org.json.simple.JSONObject;

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

    public ArrayList<Shelter> getShelters() {
    ArrayList<Shelter> shelters = new ArrayList<>();

    DataManager dataManager = new DataManager(JSONFile.SHELTERS.getFilename());
    ArrayList<JSONObject> shelterData = dataManager.readJSON();

    if (shelterData == null) {
        return shelters;
    }

    for (JSONObject jsonObject : shelterData) {
        String name = (String) jsonObject.get("name");
        int capacity = ((Long) jsonObject.get("capacity")).intValue();

        Shelter shelter = new Shelter(name, null, capacity, null);
        shelter.setOccupancy(((Long) jsonObject.get("occupancy")).intValue());
        shelter.setStatus(ShelterStatus.valueOf((String) jsonObject.get("status")));

        shelters.add(shelter);
    }

    return shelters;
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

