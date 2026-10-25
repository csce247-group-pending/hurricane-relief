package com.pending.model;

import java.awt.image.DataBuffer;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pending.location.MapTools;
import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.*;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Controls application actions and stores the currently logged-in user.
 * This class uses the Database class to load and manage project data.
 */
public class Application {
    private static Application application;
    private Database database;
    private User currentUser;
    private Location location;
    private Shelter currShelter;

    /**
     * Creates the application and loads saved database information.
     */
    private Application() {
        database = Database.getInstance();
        database.read();
        currentUser = null;
        location = null;
        currShelter = null;
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

        return database.createAccount(firstName, lastName, dateOfBirth,  new ArrayList<>(),
                password, location, userContact, emergencyContact);
    }

    public boolean requestCreateAccount(String firstName, String lastName, LocalDate dateOfBirth, String password, Location location, Contact userContact, Contact emergencyContact) {
        return database.createAccount(firstName, lastName, dateOfBirth,  new ArrayList<>(),
                password, location, userContact, emergencyContact);
    }

    /**
     * Attempts to log in a user with an email address and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return true if login succeeds; otherwise false
     */
    public boolean attemptLogin(String email, String password) {
        currentUser = database.attemptLogin(email, password);

        if (currentUser == null) {
            return false;
        }

        updateLocation(currentUser.getLocation());
        return true;
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        currentUser = null;
        location = null;
        currShelter = null;
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
    public boolean createReliefRequest(String description) {
        return database.createReliefRequest(currentUser, description, currentUser.getLocation(), new ArrayList<LivingBeing>(List.of(currentUser)));
    }

    public boolean createReliefRequest(String description, ArrayList<LivingBeing> livingBeings) {
        return database.createReliefRequest(currentUser, description, currentUser.getLocation(), livingBeings);
    }

    public boolean createReliefRequest(User author, String description) {
        return database.createReliefRequest(author, description, currentUser.getLocation(), new ArrayList<LivingBeing>(List.of(currentUser)));
    }

    public boolean createReliefRequest(User author, String description, ArrayList<LivingBeing> livingBeings) {
        return database.createReliefRequest(author, description, currentUser.getLocation(), livingBeings);
    }

    public boolean acceptReliefRequest(ReliefRequest reliefRequest) {
        if (currentUser == null) {
            return false;
        } 
        if(currentUser instanceof Volunteer volunteer) {
            return volunteer.acceptReliefRequest(reliefRequest);
        }
        return false;
    }

    /**
     * 
     * 
     * @param reliefRequest
     * @return
     */
    public boolean openReliefRequest(ReliefRequest reliefRequest) {
        if (currentUser == null) {
            return false;
        } 
        if(currentUser instanceof Volunteer volunteer) {
            return volunteer.openReliefRequest(reliefRequest);
        }
        return false;
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

    public User getCurrentUser() {
        return currentUser;
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
     * Gets the time to all of the nearest shelters to the User
     * 
     * @return ArrayList<Double> of the times in order that shelters are read
     */
    public ArrayList<Double> getShelterTimes() {
        if (location == null) {
            System.out.println("[Error] There is no current location");
            return null;
        }

        ArrayList<Shelter> shelters = database.getShelters();
        ArrayList<Double> shelterTimes = new ArrayList<>();
        try {
            for(Shelter shelter: shelters) {
                shelterTimes.add(MapTools.getInstance().getDrivingTime(location, shelter.getLocation()));
            }
        } catch (Exception e) {
            System.out.println("There was a problem finding time to Shelter.");
            e.printStackTrace();
        }

        return shelterTimes;
    }

    /**
     * Completes a relief request.
     * This action will require a Shelter Manager role check.
     */
    public boolean completeRequest(ReliefRequest reliefRequest) {
        if (currentUser == null) {
            return false;
        }

        return currentUser != null && currentUser.closeReliefRequest(reliefRequest);
    }

    /**
     * Starts a relief request.
     * This action will require a Volunteer role check.
     */
    public void startRequest() {
        // must role check for Volunteer
    }

    public boolean checkIntoShelter(ArrayList<LivingBeing> livingBeings) {
        return currentUser != null && currentUser.checkIntoShelter(livingBeings);
    }

    public ArrayList<ReliefRequest> getReliefRequests() {
        if (currentUser instanceof Volunteer volunteer) {
            return database.getSortedReliefRequests(volunteer.getSorter());
        } else {
            return database.getRequests();
        }
    }

    public ArrayList<Shelter> getSortedShelters() {
        if (currentUser == null) {
            System.out.println("No user logged in");
            return null;
        }

        // check for volunteer
        if (currentUser instanceof Volunteer volunteer) {
            // check for no task on volunteer
            if (volunteer.getTask() == null) {
                System.out.println("No Current Task");
                return null;
            }
            // get list of shelters sorted 
            return database.getSortedShelters(
                volunteer.getTask()
            );
        } else {
            System.out.println("Not a volunteer");
            return null;
        }
        
    }

    public Shelter getShelter() {
        return currShelter;
    }

    public void setShelter(Shelter shelter) {
        this.currShelter = shelter;
    }
}