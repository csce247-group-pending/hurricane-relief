package com.pending.model.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import com.pending.location.MapTools;
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Stores and manages all application data lists.
 * This class reads data from JSON files and provides access to users,
 * contacts, locations, shelters, relief requests, hurricanes, and
 * living beings.
 */
public class Database {
    private final DataList<User> users;
    private final DataList<ReliefRequest> requests;
    private ArrayList<UUID> blacklist;
    private final DataList<Hurricane> hurricanes;
    private final DataList<Shelter> shelters;
    private final DataList<Contact> contacts;
    private final DataList<Location> locations;
    private final DataList<LivingBeing> livingBeings;
    private final DataList<Volunteer> volunteers;
    private final DataList<ShelterManager> shelterManagers;

    private HashMap<Class<? extends Serializable>,
            DataList<? extends Serializable>> dataLists;

    private static Database database;

    /**
     * Creates the database and initializes each type of data list.
     */
    private Database() {
        dataLists = new HashMap<>();

        users = DataList.getInstance(User.class);
        dataLists.put(User.class, users);

        requests = DataList.getInstance(ReliefRequest.class);
        dataLists.put(ReliefRequest.class, requests);

        hurricanes = DataList.getInstance(Hurricane.class);
        dataLists.put(Hurricane.class, hurricanes);

        shelters = DataList.getInstance(Shelter.class);
        dataLists.put(Shelter.class, shelters);

        contacts = DataList.getInstance(Contact.class);
        dataLists.put(Contact.class, contacts);

        locations = DataList.getInstance(Location.class);
        dataLists.put(Location.class, locations);

        livingBeings = DataList.getInstance(LivingBeing.class);
        dataLists.put(LivingBeing.class, livingBeings);

        volunteers = DataList.getInstance(Volunteer.class);
        dataLists.put(Volunteer.class, volunteers);

        shelterManagers = DataList.getInstance(ShelterManager.class);
        dataLists.put(ShelterManager.class, shelterManagers);
    }

    /**
     * Reads every data list from its JSON file and connects related objects.
     */
    public void read() {
        for (DataList<?> dataList : dataLists.values()) {
            dataList.read();
        }

        resolveReferences();
        users.getAll().addAll(volunteers.getAll());
        users.getAll().addAll(shelterManagers.getAll());
    }

    /**
     * Writes every data list to its JSON file.
     */
    public void write() {
        for (Class<? extends Serializable> key : dataLists.keySet()) {
            write(key);
        }
    }

    /**
     * Writes a specific data list to its related JSON file.
     *
     * @param key the Serializable class whose data should be written
     */
    public void write(Class<? extends Serializable> key) {
        if (key == User.class) {
            for (Volunteer volunteer : volunteers.getAll()) {
                if (users.getAll().contains(volunteer)) {
                    users.remove(volunteer);
                }
            }

            for (ShelterManager shelterManager : shelterManagers.getAll()) {
                if (users.getAll().contains(shelterManager)) {
                    users.remove(shelterManager);
                }
            }

            for (DataList<?> dataList : dataLists.values()) {
                dataList.write();
            }

            users.write();
            users.getAll().addAll(volunteers.getAll());
            users.getAll().addAll(shelterManagers.getAll());
        } else if (dataLists.containsKey(key)) {
            dataLists.get(key).write();
        }
    }

    /**
     * Gets the single shared Database instance.
     *
     * @return the Database instance
     */
    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }

        return database;
    }

    /**
     * Reads every registered data list from its JSON file.
     */
    public void readDataLists() {
        for (DataList<? extends Serializable> dataList : dataLists.values()) {
            dataList.read();
        }
    }

    /**
     * Starts a basic database read operation.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        getInstance().read();
    }

    /**
     * Connects objects that reference other objects by UUID.
     */
    private void resolveReferences() {
        users.resolveReferences();
        volunteers.resolveReferences();
        shelterManagers.resolveReferences();
        shelters.resolveReferences();
        livingBeings.resolveReferences();
        requests.resolveReferences();
    }

    /**
     * Attempts to find and log in a user using an email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return the matching User, or null if login fails
     */
    public User attemptLogin(String email, String password) {
        User user = users.getObject(
                u -> u.getUserContact() != null
                        && email.equals(u.getUserContact().getEmail())
        );

        return user == null || !password.equals(user.getPassword())
                ? null : user;
    }

    /**
     * Creates a user account and saves its user, contacts, and location.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param reliefNeeds the user's relief needs
     * @param password the user's password
     * @param location the user's location
     * @param userContact the user's primary contact information
     * @param emergencyContact the user's emergency contact information
     * @return true if the account was created successfully; otherwise false
     */
    public boolean createAccount(String firstName, String lastName,
            LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
            String password, Location location, Contact userContact,
            Contact emergencyContact) {

        User existingUser = users.getObject(
                user -> userContact.getEmail()
                        .equals(user.getUserContact().getEmail())
        );

        if (existingUser != null) {
            return false;
        }

        User newUser = new User(firstName, lastName, dateOfBirth,
                reliefNeeds, password, location, userContact,
                emergencyContact);

        boolean locationAdded = locations.add(location);
        boolean userContactAdded = contacts.add(userContact);
        boolean emergencyContactAdded = contacts.add(emergencyContact);
        boolean userAdded = users.add(newUser);

        if (!((location == null || locationAdded)
                && (userContact == null || userContactAdded)
                && (emergencyContact == null || emergencyContactAdded)
                && userAdded)) {
            return false;
        }

        locations.write();
        contacts.write();
        users.write();

        return true;
    }

    /**
     * Creates and stores a relief request from a user.
     * The request's relief types are gathered from every member of the group.
     *
     * @param author the user creating the request
     * @param description a description of the requested assistance
     * @param location the location where assistance is needed
     * @param livingBeings the people or pets included in the request
     * @param contactInfo the contact information for the request
     * @return true if the request was added successfully; otherwise false
     */
    public boolean createReliefRequest(User author, String description,
            Location location, ArrayList<LivingBeing> livingBeings,
            Contact contactInfo) {

        HashSet<ReliefType> reliefTypes = new HashSet<>();

        for (LivingBeing livingBeing : livingBeings) {
            this.livingBeings.add(livingBeing);
            reliefTypes.addAll(livingBeing.getReliefNeeds());
        }

        ReliefRequest reliefRequest = new ReliefRequest(
                description, location, ReliefStatus.REQUESTED,
                livingBeings, new ArrayList<>(reliefTypes), contactInfo);

        if (author != null) {
            author.addUserReliefRequest(reliefRequest);
        }

        boolean returnValue = requests.add(reliefRequest);
        requests.write();
        users.write();
        this.livingBeings.write();

        return returnValue;
    }

    /**
     * Gets all living beings stored by the system.
     *
     * @return the list of living beings
     */
    public ArrayList<LivingBeing> getLivingBeings() {
        return livingBeings.getAll();
    }

    /**
     * Gets all users, volunteers, and shelter managers.
     *
     * @return the list of users
     */
    public ArrayList<User> getUsers() {
        return users.getAll();
    }

    /**
     * Gets users that match a filter.
     *
     * @param filter the user filter to apply
     * @return a list of matching users
     */
    public ArrayList<User> getUsers(Filter<User> filter) {
        return users.filterByAttribute(filter);
    }

    /**
     * Gets all relief requests.
     *
     * @return the list of relief requests
     */
    public ArrayList<ReliefRequest> getRequests() {
        return requests.getAll();
    }

    /**
     * Gets relief requests that match a filter.
     *
     * @param filter the relief-request filter to apply
     * @return a list of matching relief requests
     */
    public ArrayList<ReliefRequest> getRequests(
            Filter<ReliefRequest> filter) {

        return requests.filterByAttribute(filter);
    }

    /**
     * Gets all hurricanes.
     *
     * @return the list of hurricanes
     */
    public ArrayList<Hurricane> getHurricanes() {
        return hurricanes.getAll();
    }

    /**
     * Gets hurricanes that match a filter.
     *
     * @param filter the hurricane filter to apply
     * @return a list of matching hurricanes
     */
    public ArrayList<Hurricane> getHurricanes(Filter<Hurricane> filter) {
        return hurricanes.filterByAttribute(filter);
    }

    /**
     * Gets all shelters.
     *
     * @return the list of shelters
     */
    public ArrayList<Shelter> getShelters() {
        return shelters.getAll();
    }

    /**
     * Gets shelters that match a filter.
     *
     * @param filter the shelter filter to apply
     * @return a list of matching shelters
     */
    public ArrayList<Shelter> getShelters(Filter<Shelter> filter) {
        return shelters.filterByAttribute(filter);
    }

    /**
     * Gets all contacts.
     *
     * @return the list of contacts
     */
    public ArrayList<Contact> getContacts() {
        return contacts.getAll();
    }

    /**
     * Gets contacts that match a filter.
     *
     * @param filter the contact filter to apply
     * @return a list of matching contacts
     */
    public ArrayList<Contact> getContacts(Filter<Contact> filter) {
        return contacts.filterByAttribute(filter);
    }

    /**
     * Gets all locations.
     *
     * @return the list of locations
     */
    public ArrayList<Location> getLocations() {
        return locations.getAll();
    }

    /**
     * Gets locations that match a filter.
     *
     * @param filter the location filter to apply
     * @return a list of matching locations
     */
    public ArrayList<Location> getLocations(Filter<Location> filter) {
        return locations.filterByAttribute(filter);
    }

    /**
     * Finds a living being by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching living being, or null if none exists
     */
    public LivingBeing getLivingBeingByUUID(UUID id) {
        return livingBeings.getObjectByUUID(id);
    }

    /**
     * Finds a user by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching user, or null if none exists
     */
    public User getUserByUUID(UUID id) {
        return users.getObjectByUUID(id);
    }

    /**
     * Finds a volunteer by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching volunteer, or null if none exists
     */
    public Volunteer getVolunteerByUUID(UUID id) {
        return volunteers.getObjectByUUID(id);
    }

    /**
     * Finds a relief request by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching relief request, or null if none exists
     */
    public ReliefRequest getReliefRequestByUUID(UUID id) {
        return requests.getObjectByUUID(id);
    }

    /**
     * Finds a hurricane by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching hurricane, or null if none exists
     */
    public Hurricane getHurricaneByUUID(UUID id) {
        return hurricanes.getObjectByUUID(id);
    }

    /**
     * Finds a shelter by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching shelter, or null if none exists
     */
    public Shelter getShelterByUUID(UUID id) {
        return shelters.getObjectByUUID(id);
    }

    /**
     * Finds a contact by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching contact, or null if none exists
     */
    public Contact getContactByUUID(UUID id) {
        return contacts.getObjectByUUID(id);
    }

    /**
     * Finds a location by its UUID.
     *
     * @param id the UUID to search for
     * @return the matching location, or null if none exists
     */
    public Location getLocationByUUID(UUID id) {
        return locations.getObjectByUUID(id);
    }

    /**
     * Gets the DataList associated with a Serializable class.
     *
     * @param cls the Serializable class to look up
     * @return the matching data list, or null if it is not registered
     */
    public DataList<? extends Serializable> get(
            Class<? extends Serializable> cls) {

        return dataLists.get(cls);
    }

    /**
     * Returns relief requests ordered from highest to lowest priority.
     *
     * @param filter calculates a priority score for each request
     * @return the relief requests ordered by priority
     */
    public ArrayList<ReliefRequest> getSortedReliefRequests(
            PriorityFilter<ReliefRequest> filter) {

        ArrayList<ReliefRequest> output = getRequests();

        output.sort(
                Comparator.comparing(filter::filter).reversed()
        );

        return output;
    }

    /**
     * Returns shelters ranked by how well they match a relief request.
     * Shelters with more matching attributes are listed first. Shelters
     * with equal match counts are ordered by driving time.
     *
     * @param task the relief request needing a shelter
     * @return a sorted list of shelters, or null if routing fails
     */
    public ArrayList<Shelter> getSortedShelters(ReliefRequest task) {
        ArrayList<Shelter> shelterList = getShelters();

        try {
            Map<Shelter, Integer> matchMap = new HashMap<>();

            for (Shelter shelter : shelterList) {
                shelter.setTimeTo(
                        MapTools.getInstance().getDrivingTime(
                                task.getLocation(),
                                shelter.getLocation())
                );

                int matches = countMatchingAttributes(shelter, task);
                matchMap.put(shelter, matches);
            }

            shelterList.sort(
                    Comparator.comparingInt(
                            (Shelter shelter) -> matchMap.get(shelter))
                            .reversed()
                            .thenComparingDouble(
                                    shelter -> shelter.getTimeTo())
            );

            return shelterList;
        } catch (Exception e) {
            System.out.println("There was a problem finding a shelter.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Counts how well a shelter matches a relief request.
     *
     * @param shelter the shelter being evaluated
     * @param task the relief request being matched
     * @return the number of matching shelter attributes
     */
    private int countMatchingAttributes(Shelter shelter,
            ReliefRequest task) {

        int count = 0;

        if (task.hasPets()
                && shelter.isPetFriendly() == task.hasPets()) {
            count++;
        }

        if (task.getTypes().contains(ReliefType.MEDICAL_CARE)
                && shelter.hasMedicalStaff()) {
            count++;
        }

        if (shelter.hasCapacity(task.getPartySizeHumans())) {
            count++;
        }

        return count;
    }

    /**
     * Exports a volunteer's assigned relief request and driving directions
     * to a text file.
     *
     * @param volunteer the volunteer completing the request
     * @param shelter the shelter selected for the trip
     * @return true if the export succeeds; otherwise false
     */
    public boolean exportTripInformation(Volunteer volunteer,
            Shelter shelter) {

        String outputFile = volunteer.getFirstName()
                + "_relief_task.txt";

        String toWrite = "Exporting Trip Information\n\n";
        toWrite += volunteer.getTask().toStringDetailed() + "\n\n";

        try {
            toWrite += "Directions From Volunteer To Task:\n";

            for (String direction : MapTools.getInstance()
                    .getDrivingDirections(
                            volunteer.getLocation(),
                            volunteer.getTask().getLocation())) {
                toWrite += "\t" + direction + "\n";
            }

            toWrite += "\n\n";
            toWrite += "Directions From Task to '"
                    + shelter.getName() + "' Shelter:\n";

            for (String direction : MapTools.getInstance()
                    .getDrivingDirections(
                            volunteer.getTask().getLocation(),
                            shelter.getLocation())) {
                toWrite += "\t" + direction + "\n";
            }
        } catch (Exception e) {
            System.out.println("Failed getting directions.");
            return false;
        }

        return volunteers.exportTripInformation(toWrite, outputFile);
    }
}