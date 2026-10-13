package com.pending.model.data;

import com.pending.model.data.serializables.*;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Database {
    private final DataList<User> users;
    private final DataList<ReliefRequest> requests;
    private ArrayList<UUID> blacklist;
    private final DataList<Hurricane> hurricanes;
    private final DataList<Shelter> shelters;
    private final DataList<Contact> contacts;

    private HashMap<Class<? extends Serializable>, DataList<? extends Serializable>> dataLists;

    private ArrayList<Admin> admins;

    private static Database database;

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

        resolveReferences();
    }

    public static Database getInstance() {
        if (database == null) return database = new Database();
        return database;
    }

    public static void main(String[] args) {
        getInstance();
    }

    private void resolveReferences() {
        for (DataList<?> dataList: dataLists.values()) {
            dataList.resolveReferences();
        }
    }

    public User attemptLogin(String email, String password) {
        User user = users.getObject(
                (u) -> email.equals(u.getUserContact().getEmail())
        );

        return user == null || !password.equals(user.getPassword()) ? null : user;
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

    public ArrayList<ReliefRequest> getRequests(Filter<ReliefRequest> filter) {
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

    public DataList<? extends Serializable> get(Class<? extends Serializable> cls) {
        return dataLists.get(cls);
    }
}