package com.pending.model.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import com.pending.model.data.serializables.*;
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
            for (Volunteer volunteer: volunteers.getAll()) {
                if (users.getAll().contains(volunteer)) {
                    users.remove(volunteer);
                }
            }
            dataList.write();
            users.getAll().addAll(volunteers.getAll());
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

        if (!locationAdded || !userContactAdded
                || !emergencyContactAdded || !userAdded) {
            return false;
        }

        locations.write();
        contacts.write();
        users.write();

        return true;
    }

    public void createReliefRequest(String description, Location location, ArrayList<LivingBeing> livingBeings, ArrayList<ReliefType> reliefTypes) {
        ReliefRequest reliefRequest = new ReliefRequest(description, location, ReliefStatus.REQUESTED, livingBeings);
        requests.add(reliefRequest);
    }

    public ArrayList<LivingBeing> getLivingBeings() {
        return livingBeings.getAll();
    }

    public ArrayList<User> getUsers() {
        return users.getAll();
    }

    public ArrayList<User> getUsers(Filter<User> filter) {
        return users.filterByAttribute(filter);
    }

    public ArrayList<ReliefRequest> getRequests() {
        return requests.getAll();
    }

    public ArrayList<ReliefRequest> getRequests(
            Filter<ReliefRequest> filter) {
        return requests.filterByAttribute(filter);
    }

    public ArrayList<Hurricane> getHurricanes() {
        return hurricanes.getAll();
    }

    public ArrayList<Hurricane> getHurricanes(Filter<Hurricane> filter) {
        return hurricanes.filterByAttribute(filter);
    }

    public ArrayList<Shelter> getShelters() {
        return shelters.getAll();
    }

    public ArrayList<Shelter> getShelters(Filter<Shelter> filter) {
        return shelters.filterByAttribute(filter);
    }

    public ArrayList<Contact> getContacts() {
        return contacts.getAll();
    }

    public ArrayList<Contact> getContacts(Filter<Contact> filter) {
        return contacts.filterByAttribute(filter);
    }

    public ArrayList<Location> getLocations() {
        return locations.getAll();
    }

    public ArrayList<Location> getLocations(Filter<Location> filter) {
        return locations.filterByAttribute(filter);
    }

    public LivingBeing getLivingBeingByUUID(UUID id) {
        return livingBeings.getObjectByUUID(id);
    }

    public User getUserByUUID(UUID id) {
        return users.getObjectByUUID(id);
    }

    public ReliefRequest getReliefRequestByUUID(UUID id) {
        return requests.getObjectByUUID(id);
    }

    public Hurricane getHurricaneByUUID(UUID id) {
        return hurricanes.getObjectByUUID(id);
    }

    public Shelter getShelterByUUID(UUID id) {
        return shelters.getObjectByUUID(id);
    }

    public Contact getContactByUUID(UUID id) {
        return contacts.getObjectByUUID(id);
    }

    public Location getLocationByUUID(UUID id) {
        return locations.getObjectByUUID(id);
    }

    public DataList<? extends Serializable> get(
            Class<? extends Serializable> cls) {
        return dataLists.get(cls);
    }

    public ArrayList<ReliefRequest> getSortedReliefRequests(
            PriorityFilter<ReliefRequest> filter) {
        ArrayList<ReliefRequest> output = getRequests();
        output.sort(Comparator.comparing(filter::filter));
        return output;
    }
}