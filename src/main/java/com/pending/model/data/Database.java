package com.pending.model.data;

import com.pending.model.*;
import com.pending.model.data.serializables.*;

import java.util.ArrayList;
import java.util.UUID;

public class Database {
    private DataList<User> users;
    private DataList<ReliefRequest> requests;
    private ArrayList<UUID> blacklist;
    private DataList<Hurricane> hurricanes;
    private DataList<Shelter> shelters;
    private DataList<Contact> contacts;

    private ArrayList<Admin> admins;

    private static Database database;

    private Database() {
        users = DataList.getInstance(User.class);
        requests = DataList.getInstance(ReliefRequest.class);
        hurricanes = DataList.getInstance(Hurricane.class);
        shelters = DataList.getInstance(Shelter.class);
        contacts = DataList.getInstance(Contact.class);
    }

    public static Database getInstance() {
        if (database == null) return database = new Database();
        return database;
    }

    public static void main(String[] args) {
        getInstance();
    }

    public User attemptLogin(String email, String password) {
        User user = users.getObject(
                (u) -> email.equals(u.getUserContact().getEmail())
        );

        return user == null || !password.equals(user.getPassword()) ? null : user;
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
}