package com.pending.model;

import java.util.ArrayList;
import java.util.UUID;

public class Database {
    private DataList<User> users;
    private DataList<Admin> admins;
    private DataList<ReliefRequest> requests;
    private ArrayList<UUID> blacklist;
    private DataList<Hurricane> hurricanes;
    private DataList<Shelter> shelters;
    private DataList<Contact> contacts;
}
