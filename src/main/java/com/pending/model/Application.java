package com.pending.model;

import java.util.ArrayList;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.User;
import com.pending.model.enums.ContactPreference;

public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;

    private Application() {
        database = Database.getInstance();
        database.read();
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
        Location location = new Location(state, city, street, zip);

        Contact userContact = new Contact(userContactEmail, userContactPhone, ContactPreference.valueOf(userContactPreference));
        Contact emergencyContact = new Contact(emergencyContactEmail, emergencyContactPhone, ContactPreference.valueOf(emergencyContactPreference));

        return database.createAccount(firstName, lastName, password,
                            location, userContact, emergencyContact);
    }

    public boolean attemptLogin(String email, String password) {
        return (currentUser = database.attemptLogin(email, password)) != null;
    }

    public void logout() {

    }

    public String getUserFullName() {
        return currentUser.getFirstName() + " " + currentUser.getLastName();
    }

    public void createReliefRequest() {

    }

    public ArrayList<Shelter> getShelters() {
        return database.getShelters();
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
    
    private Location getLocation() {
        return location;
    }

    private void updateLocation(Location location) {
        this.location = location;
    }
}

