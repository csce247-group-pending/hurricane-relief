package com.pending.model.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

<<<<<<< HEAD
import com.pending.model.data.serializables.Contact;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Location;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.Serializable;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.User;
=======
import com.pending.model.data.serializables.*;
>>>>>>> c070dad (Begin storage for living beings)
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Stores and manages all application data lists.
 * This class reads data from JSON files and provides access to users,
 * contacts, locations, shelters, relief requests, and hurricanes.
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

    private HashMap<Class<? extends Serializable>, DataList<? extends Serializable>> dataLists;


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

    }

    /**
     * Reads every data list from its JSON file and connects related objects.
     */
    public void read() {
        for (DataList<?> dataList : dataLists.values()) {
            dataList.read();
        }
        resolveReferences();
    }

    /**
     * Writes every data list to its JSON file.
     */
    public void write() {
        for (DataList<?> dataList : dataLists.values()) {
            dataList.write();
        }
    }

    /**
     * Gets the single Database instance.
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
     * Reads every data list from its JSON file.
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
        for (DataList<?> dataList : dataLists.values()) {
            dataList.resolveReferences();
        }
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
                u -> email.equals(u.getUserContact().getEmail())
        );

        return user == null || !password.equals(user.getPassword()) ? null : user;
    }

    /**
     * Creates a user account and saves its user, contacts, and location.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth
     * @param password the user's password
     * @param location the user's location
     * @param userContact the user's primary contact information
     * @param emergencyContact the user's emergency contact information
     * @return true if the account was created successfully; otherwise false
     */
    public boolean createAccount(String firstName, String lastName,
                                 LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds,
                                 String password,
                                 Location location, Contact userContact,
                                 Contact emergencyContact) {

        User existingUser = users.getObject(
                user -> userContact.getEmail().equals(user.getUserContact().getEmail())
        );

        if (existingUser != null) {
            return false;
        }

        User newUser = new User(firstName, lastName, dateOfBirth, reliefNeeds, password,
                location, userContact, emergencyContact);

        boolean locationAdded = locations.add(location);
        boolean userContactAdded = contacts.add(userContact);
        boolean emergencyContactAdded = contacts.add(emergencyContact);
        boolean userAdded = users.add(newUser);

        if (!locationAdded || !userContactAdded || !emergencyContactAdded || !userAdded) {
            return false;
        }

        locations.write();
        contacts.write();
        users.write();

        return true;
    }

    public void createReliefRequest(String description, Location location,
                                    ReliefStatus status, ArrayList<LivingBeing> livingBeings) {
        ReliefRequest reliefRequest = new ReliefRequest(description, location, status, livingBeings);
        requests.add(reliefRequest);
    }

    /**
     * Gets all users.
     *
     * @return a list of all users
     */
    public ArrayList<User> getUsers() {
        return users.getAll();
    }

    /**
     * Gets users that match a filter.
     *
     * @param filter the filter used to select users
     * @return a list of matching users
     */
    public ArrayList<User> getUsers(Filter<User> filter) {
        return users.filterByAttribute(filter);
    }

    /**
     * Gets all relief requests.
     *
     * @return a list of all relief requests
     */
    public ArrayList<ReliefRequest> getRequests() {
        return requests.getAll();
    }

    /**
     * Gets relief requests that match a filter.
     *
     * @param filter the filter used to select relief requests
     * @return a list of matching relief requests
     */
    public ArrayList<ReliefRequest> getRequests(Filter<ReliefRequest> filter) {
        return requests.filterByAttribute(filter);
    }

    /**
     * Gets all hurricanes.
     *
     * @return a list of all hurricanes
     */
    public ArrayList<Hurricane> getHurricanes() {
        return hurricanes.getAll();
    }

    /**
     * Gets hurricanes that match a filter.
     *
     * @param filter the filter used to select hurricanes
     * @return a list of matching hurricanes
     */
    public ArrayList<Hurricane> getHurricanes(Filter<Hurricane> filter) {
        return hurricanes.filterByAttribute(filter);
    }

    /**
     * Gets all shelters.
     *
     * @return a list of all shelters
     */
    public ArrayList<Shelter> getShelters() {
        return shelters.getAll();
    }

    /**
     * Gets shelters that match a filter.
     *
     * @param filter the filter used to select shelters
     * @return a list of matching shelters
     */
    public ArrayList<Shelter> getShelters(Filter<Shelter> filter) {
        return shelters.filterByAttribute(filter);
    }

    /**
     * Gets all contacts.
     *
     * @return a list of all contacts
     */
    public ArrayList<Contact> getContacts() {
        return contacts.getAll();
    }

    /**
     * Gets contacts that match a filter.
     *
     * @param filter the filter used to select contacts
     * @return a list of matching contacts
     */
    public ArrayList<Contact> getContacts(Filter<Contact> filter) {
        return contacts.filterByAttribute(filter);
    }

    /**
     * Gets all locations.
     *
     * @return a list of all locations
     */
    public ArrayList<Location> getLocations() {
        return locations.getAll();
    }

    /**
     * Gets locations that match a filter.
     *
     * @param filter the filter used to select locations
     * @return a list of matching locations
     */
    public ArrayList<Location> getLocations(Filter<Location> filter) {
        return locations.filterByAttribute(filter);
    }

    /**
     * Finds a user by UUID.
     *
     * @param id the user's UUID
     * @return the matching user, or null if not found
     */
    public User getUserByUUID(UUID id) {
        return users.getObjectByUUID(id);
    }

    /**
     * Finds a relief request by UUID.
     *
     * @param id the relief request UUID
     * @return the matching relief request, or null if not found
     */
    public ReliefRequest getReliefRequestByUUID(UUID id) {
        return requests.getObjectByUUID(id);
    }

    /**
     * Finds a hurricane by UUID.
     *
     * @param id the hurricane UUID
     * @return the matching hurricane, or null if not found
     */
    public Hurricane getHurricaneByUUID(UUID id) {
        return hurricanes.getObjectByUUID(id);
    }

    /**
     * Finds a shelter by UUID.
     *
     * @param id the shelter UUID
     * @return the matching shelter, or null if not found
     */
    public Shelter getShelterByUUID(UUID id) {
        return shelters.getObjectByUUID(id);
    }

    /**
     * Finds a contact by UUID.
     *
     * @param id the contact UUID
     * @return the matching contact, or null if not found
     */
    public Contact getContactByUUID(UUID id) {
        return contacts.getObjectByUUID(id);
    }

    /**
     * Finds a location by UUID.
     *
     * @param id the location UUID
     * @return the matching location, or null if not found
     */
    public Location getLocationByUUID(UUID id) {
        return locations.getObjectByUUID(id);
    }

    /**
     * Gets the data list associated with a Serializable class.
     *
     * @param cls the Serializable class
     * @return the matching data list
     */
    public DataList<? extends Serializable> get(Class<? extends Serializable> cls) {
        return dataLists.get(cls);
    }

    public ArrayList<ReliefRequest> getSortedReliefRequests(PriorityFilter<ReliefRequest> filter) {
        ArrayList<ReliefRequest> output = getRequests();
        output.sort(Comparator.comparing(filter::filter));
        return output;
    }
}