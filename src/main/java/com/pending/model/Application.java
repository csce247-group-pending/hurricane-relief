package com.pending.model;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.pending.model.data.DataManager;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Resource;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.User;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.ShelterStatus;

public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;

    private Application() {
        // database = Database.getInstance();
        currentUser = null;
        location = null;
    }

    public static Application getInstance() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    public boolean requestCreateAccount(String firstName, String lastName, String password,
            String address, 
            String userContactEmail, String userContactPhone, String userContactPreference,
            String emergencyContactEmail, String emergencyContactPhone, String emergencyContactPreference) {

        String[] locationInformation = address.split(", ");
        String state = locationInformation[2];
        String city = locationInformation[1];
        String street = locationInformation[0];
        String zip = locationInformation[3];
        Location location = new Location(state, city, "", street, zip);

        Contact userContact = new Contact(userContactEmail, userContactPhone, ContactPreference.valueOf(userContactPreference));
        Contact emergencyContact = new Contact(emergencyContactEmail, emergencyContactPhone, ContactPreference.valueOf(emergencyContactPreference));

        currentUser = database.createAccount(firstName, lastName, password,
                            location, userContact, emergencyContact);
        return currentUser != null;
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

