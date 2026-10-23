package com.pending.model;

import java.time.LocalDate;
import java.util.ArrayList;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.User;
import com.pending.model.enums.ContactPreference;

/**
 * Controls application actions and stores the currently logged-in user.
 * This class uses the Database class to load and manage project data.
 */
public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;

    /**
     * Creates the application and loads saved database information.
     */
    private Application() {
        database = Database.getInstance();
        database.read();
        currentUser = null;
        location = null;
    }

    /**
     * Gets the single Application instance.
     *
     * @return the Application instance
     */
    public static Application getInstance() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    /**
     * Creates a new user account with contact and location information.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param password the user's password
     * @param address the user's address as a comma-separated string
     * @param userContactEmail the user's email address
     * @param userContactPhone the user's phone number
     * @param userContactPreference the user's preferred contact method
     * @param emergencyContactEmail the emergency contact's email address
     * @param emergencyContactPhone the emergency contact's phone number
     * @param emergencyContactPreference the emergency contact's preferred contact method
     * @return true if the account was created successfully; otherwise false
     */
    public boolean requestCreateAccount(String firstName, String lastName,
                                        LocalDate dateOfBirth, String password,
                                        String address,
                                        String userContactEmail, String userContactPhone,
                                        String userContactPreference,
                                        String emergencyContactEmail,
                                        String emergencyContactPhone,
                                        String emergencyContactPreference) {

        String[] locationInformation = address.split(", ");
        String state = locationInformation[2];
        String city = locationInformation[1];
        String street = locationInformation[0];
        String zip = locationInformation[3];

        Location location = new Location(state, city, street, zip);

        Contact userContact = new Contact(userContactEmail, userContactPhone,
                ContactPreference.valueOf(userContactPreference));

        Contact emergencyContact = new Contact(emergencyContactEmail,
                emergencyContactPhone,
                ContactPreference.valueOf(emergencyContactPreference));

        return database.createAccount(firstName, lastName, dateOfBirth, password,
                location, userContact, emergencyContact);
    }

    /**
     * Attempts to log in a user with an email address and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return true if login succeeds; otherwise false
     */
    public boolean attemptLogin(String email, String password) {
        return (currentUser = database.attemptLogin(email, password)) != null;
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Gets the full name of the currently logged-in user.
     *
     * @return the user's first and last name
     */
    public String getUserFullName() {
        return currentUser.getFirstName() + " " + currentUser.getLastName();
    }

    /**
     * Creates a new relief request.
     * This method is not implemented yet.
     */
    public void createReliefRequest() {
    }

    /**
     * Gets all shelters as text for display in the user interface.
     *
     * @return a list of shelter descriptions
     */
    public ArrayList<String> getShelters() {
        ArrayList<String> shelterStrings = new ArrayList<>();

        for (Shelter shelter : database.getShelters()) {
            shelterStrings.add(shelter.toString());
        }

        return shelterStrings;
    }

    /**
     * Gets all relief requests as text for display in the user interface.
     *
     * @return a list of relief request descriptions
     */
    public ArrayList<String> getRequests() {
        ArrayList<String> requestStrings = new ArrayList<>();

        for (ReliefRequest request : database.getRequests()) {
            requestStrings.add(request.toString());
        }

        return requestStrings;
    }

    /**
     * Gets hurricane data for display in the user interface.
     *
     * @return a list of hurricane descriptions
     */
    public ArrayList<String> getHurricaneData() {
        return null;
    }

    /**
     * Gets all contacts as text for display in the user interface.
     *
     * @return a list of contact descriptions
     */
    public ArrayList<String> getContacts() {
        ArrayList<String> contactStrings = new ArrayList<>();

        for (Contact contact : database.getContacts()) {
            contactStrings.add(contact.toString());
        }

        return contactStrings;
    }

    /**
     * Gets the current location as text.
     *
     * @return the current location description
     */
    public String getLocation() {
        return location.toString();
    }

    /**
     * Updates the current location.
     *
     * @param location the new current location
     */
    public void updateLocation(Location location) {
        this.location = location;
    }

    /**
     * Completes a relief request.
     * This action will require a Shelter Manager role check.
     */
    public void completeRequest() {
        // must role check for Shelter Manager
    }

    /**
     * Starts a relief request.
     * This action will require a Volunteer role check.
     */
    public void startRequest() {
        // must role check for Volunteer
    }
}