package com.pending.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pending.location.MapTools;
import com.pending.model.data.Database;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ContactPreference;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Controls application actions and stores the currently logged-in user.
 * This class uses the Database class to load and manage project data.
 */
public class Application {
    private static Application application;

    private Database database;

    // Session Variables
    private User currentUser;
    private Location location;
    private Shelter currShelter;
    private ArrayList<LivingBeing> pendingGroupMembers;



    /**
     * Creates the application and loads saved database information.
     */
    private Application() {
        database = Database.getInstance();
        database.read();
        currentUser = null;
        location = null;
        currShelter = null;
        pendingGroupMembers = null;
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
     * Creates a new user account using text-based location and contact data.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param password the user's password
     * @param address the user's comma-separated address
     * @param userContactEmail the user's email address
     * @param userContactPhone the user's phone number
     * @param userContactPreference the user's preferred contact method
     * @param emergencyContactEmail the emergency contact email address
     * @param emergencyContactPhone the emergency contact phone number
     * @param emergencyContactPreference the emergency contact preference
     * @return true if the account was created; otherwise false
     */
    public boolean requestCreateAccount(String firstName, String lastName,
            LocalDate dateOfBirth, String password, String address,
            String userContactEmail, String userContactPhone,
            String userContactPreference, String emergencyContactEmail,
            String emergencyContactPhone,
            String emergencyContactPreference) {

        String[] locationInformation = address.split(", ");
        String state = locationInformation[2];
        String city = locationInformation[1];
        String street = locationInformation[0];
        String zip = locationInformation[3];

        Location location = new Location(state, city, street, zip);

        Contact userContact = new Contact(userContactEmail,
                userContactPhone,
                ContactPreference.valueOf(userContactPreference));

        Contact emergencyContact = new Contact(emergencyContactEmail,
                emergencyContactPhone,
                ContactPreference.valueOf(emergencyContactPreference));

        return database.createAccount(firstName, lastName, dateOfBirth,
                new ArrayList<>(), password, location, userContact,
                emergencyContact);
    }

    /**
     * Creates a new user account using Location and Contact objects.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param password the user's password
     * @param location the user's location
     * @param userContact the user's primary contact information
     * @param emergencyContact the user's emergency contact information
     * @return true if the account was created; otherwise false
     */
    public boolean requestCreateAccount(String firstName, String lastName,
            LocalDate dateOfBirth, String password, Location location,
            Contact userContact, Contact emergencyContact) {

        return database.createAccount(firstName, lastName, dateOfBirth,
                new ArrayList<>(), password, location, userContact,
                emergencyContact);
    }

    /**
     * Attempts to log in a user with an email address and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return true if login succeeds; otherwise false
     */
    public String attemptLogin(String email, String password) {
        currentUser = database.attemptLogin(email, password);

        if (currentUser == null) {
            return null;
        }

        updateSessionLocation(currentUser.getLocation());
        return getUserString();
    }

    public String getUserString() {
        if (currentUser == null) {
            return null;
        }

        return currentUser.toString();
    }

    public String getVolunteerProfile() {
        if(currentUser == null) return null;
        if(currentUser instanceof Volunteer volunteer) {
            return volunteer.toString();
        } else {
            return "Not a volunteer";
        }
    }

    /**
     * Logs out the current user and clears session information.
     */
    public void logout() {
        currentUser = null;
        location = null;
        currShelter = null;
    }

    /**
     * Gets the logged-in user's full name.
     *
     * @return the user's first and last name
     */
    public String getUserFullName() {
        return currentUser.getFirstName() + " "
                + currentUser.getLastName();
    }

    /**
     * Creates a relief request for the current user.
     *
     * @param description the request description
     * @return true if the request was created; otherwise false
     */
    public boolean createReliefRequest(String description) {
        return database.createReliefRequest(currentUser, description,
            currentUser.getLocation(), new ArrayList<LivingBeing>(List.of(currentUser)),
            currentUser.getUserContact());
    }

    public boolean createReliefRequest(String description, ArrayList<LivingBeing> livingBeings) {
        return database.createReliefRequest(currentUser, description,
             currentUser.getLocation(), livingBeings, currentUser.getUserContact());
    }

    /**
     * Creates a relief request for a specified user.
     *
     * @param author the user creating the request
     * @param description the request description
     * @return true if the request was created; otherwise false
     */
    public boolean createReliefRequest(User author, String description) {
        return database.createReliefRequest(author, description,
            currentUser.getLocation(), new ArrayList<LivingBeing>(List.of(currentUser)), author.getUserContact());
    }

    public boolean createReliefRequest(User author, String description, ArrayList<LivingBeing> livingBeings) {
        return database.createReliefRequest(author, description,
            currentUser.getLocation(), livingBeings, author.getUserContact());
    }

    /**
     * Opens a relief request when the current user is a volunteer.
     *
     * @param reliefRequest the request to open
     * @return true if the request was opened; otherwise false
     */
    public boolean openReliefRequest(ReliefRequest reliefRequest) {
        if (currentUser instanceof Volunteer volunteer) {
            if(volunteer.openReliefRequest(reliefRequest)) {
                database.write(Volunteer.class);
                return true;
            }
        }

        return false;
    }

    /**
     * Assigns a relief request to the logged-in volunteer.
     *
     * @param reliefRequest the request to accept
     * @return true if the volunteer accepted the request; otherwise false
     */
    public boolean acceptReliefRequest(ReliefRequest reliefRequest) {
        if (currentUser instanceof Volunteer volunteer
                && volunteer.acceptReliefRequest(reliefRequest)) {

            database.write(ReliefRequest.class);
            return true;
        }

        return false;
    }

    /**
     * Completes a specific request using the logged-in shelter manager.
     *
     * @param reliefRequest the request to complete
     * @return true if the request was completed; otherwise false
     */
    public boolean completeRequest(ReliefRequest reliefRequest) {
        return currentUser != null
                && currentUser.closeReliefRequest(reliefRequest);
    }

    /**
     * Completes the current shelter manager's assigned request.
     *
     * @return true if the request was completed; otherwise false
     */
    public boolean closeReliefRequest() {
        if (!(currentUser instanceof ShelterManager shelterManager)) {
            System.out.println("Only Shelter Managers can complete tasks.");
            return false;
        }

        if (shelterManager.getTask() == null) {
            System.out.println("No relief request is assigned to this manager.");
            return false;
        }

        boolean completed = shelterManager.closeReliefRequest(
                shelterManager.getTask());

        if (completed) {
            database.write(ReliefRequest.class);
        }

        return completed;
    }

    /**
     * Gets all shelters as display text.
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
     * Gets all relief requests as display text.
     *
     * @return a list of request descriptions
     */
    public ArrayList<String> getRequests() {
        ArrayList<String> requestStrings = new ArrayList<>();

        for (ReliefRequest request : database.getRequests()) {
            requestStrings.add(request.toString());
        }

        return requestStrings;
    }

    /**
     * Gets hurricane data for display.
     *
     * @return hurricane data, when implemented
     */
    public ArrayList<String> getHurricaneData() {
        return null;
    }

    /**
     * Gets all contacts as display text.
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
     * Gets the logged-in user's current location.
     *
     * @return the location as text
     */
    public String getLocation() {
        return location.toString();
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return the current user, or null if logged out
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Updates the current location.
     *
     * @param location the new current location
     */
    public void updateSessionLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets driving times from the current user location to each shelter.
     *
     * @return shelter driving times, or null if unavailable
     */
    public ArrayList<Double> getShelterTimes() {
        if (location == null) {
            System.out.println("[Error] There is no current location");
            return null;
        }

        ArrayList<Shelter> shelters = database.getShelters();
        ArrayList<Double> shelterTimes = new ArrayList<>();

        try {
            for (Shelter shelter : shelters) {
                shelterTimes.add(MapTools.getInstance().getDrivingTime(
                        location, shelter.getLocation()));
            }
        } catch (Exception e) {
            System.out.println("There was a problem finding time to shelter.");
            e.printStackTrace();
        }

        return shelterTimes;
    }

    /**
     * Checks living beings into the current shelter manager's shelter.
     *
     * @param livingBeings the people or pets to check in
     * @return true if check-in succeeds; otherwise false
     */
    public boolean checkIntoShelter(ArrayList<LivingBeing> livingBeings) {
        return currentUser != null
                && currentUser.checkIntoShelter(livingBeings);
    }

    /**
     * Gets relief requests, sorted for volunteers when appropriate.
     *
     * @return relief requests available to the current user
     */
    public ArrayList<ReliefRequest> getReliefRequests() {
        if (currentUser instanceof Volunteer volunteer) {
            return database.getSortedReliefRequests(volunteer.getSorter());
        }

        return database.getRequests();
    }


    public String getCurrentReliefRequest() {
        if (currentUser == null) return "No Current User";
        
        if (currentUser instanceof Volunteer volunteer) {
            ReliefRequest currTask = volunteer.getTask();
            if (currTask == null) return "No Current Task";

            return currTask.toStringDetailed();
        }

        return "User is not a Volunteer";
    }

    /**
     * Gets relief requests as formatted text strings, sorted for volunteers when appropriate.
     *
     * @return a list of relief request descriptions available to the current user
     */
    public ArrayList<String> getReliefRequestStrings() {
        ArrayList<ReliefRequest> requests;

        if (currentUser instanceof Volunteer volunteer) {
            requests = database.getSortedReliefRequests(volunteer.getSorter());
        } else {
            requests = database.getRequests();
        }

        ArrayList<String> requestStrings = new ArrayList<>();
        if (requests != null) {
            for (ReliefRequest request : requests) {
                requestStrings.add(request.toString());
            }
        }

        return requestStrings;
    }

    /**
     * Gets shelters sorted for the current volunteer's assigned request.
     *
     * @return sorted shelters, or null when unavailable
     */
    public ArrayList<Shelter> getSortedShelters() {
        if (!(currentUser instanceof Volunteer volunteer)) {
            System.out.println("Only volunteers can view sorted shelters.");
            return null;
        }

        if (volunteer.getTask() == null) {
            System.out.println("No current task is assigned.");
            return null;
        }

        return database.getSortedShelters(volunteer.getTask());
    }

    /**
     * Gets the shelter selected for the current trip.
     *
     * @return the selected shelter
     */
    public Shelter getShelter() {
        return currShelter;
    }

    /**
     * Sets the shelter selected for the current trip.
     *
     * @param shelter the selected shelter
     */
    public void setShelter(Shelter shelter) {
        currShelter = shelter;
    }

    /**
     * Exports trip information for the current volunteer and selected shelter.
     *
     * @return true if the export succeeds; otherwise false
     */
    public boolean exportTripInformation() {
        if (currentUser == null || currShelter == null) {
            System.out.println("[Error] Not logged in or no shelter selected");
            return false;
        }

        if (currentUser instanceof Volunteer volunteer) {
            return database.exportTripInformation(volunteer, currShelter);
        }

        System.out.println("Only volunteers can export trip information.");
        return false;

    }

    /**
     * Sets the relief needs for the currently logged-in user.
     */
    public boolean setCurrentUserReliefNeeds(ArrayList<ReliefType> reliefNeeds) {
        if (currentUser == null) {
            return false;
        }
        currentUser.setReliefNeeds(reliefNeeds);
        return true;
    }

    /**
     * Starts a new family/group list for a relief request and automatically includes currentUser.
     */
    public void startNewReliefGroup() {
        pendingGroupMembers = new ArrayList<>();
        if (currentUser != null) {
            pendingGroupMembers.add(currentUser);
        }
    }

    /**
     * Instantiates a LivingBeing inside the Facade and adds it to the pending relief group.
     */
    public void addMemberToReliefGroup(String firstName, String lastName, LocalDate dateOfBirth,
                                    LivingBeingType type, ArrayList<ReliefType> reliefNeeds) {
        LivingBeing member = new LivingBeing(firstName, lastName, dateOfBirth, type, reliefNeeds);
        pendingGroupMembers.add(member);
    }

    /**
     * Submits the relief request using the staged family/group members.
     */
    public boolean submitGroupReliefRequest(String description) {
        if (currentUser == null) {
            return false;
        }
        if (pendingGroupMembers.isEmpty()) {
            pendingGroupMembers.add(currentUser);
        }
        boolean success = database.createReliefRequest(
                currentUser,
                description,
                currentUser.getLocation(),
                new ArrayList<>(pendingGroupMembers),
                currentUser.getUserContact()
        );
        pendingGroupMembers.clear();
        return success;
    }

}