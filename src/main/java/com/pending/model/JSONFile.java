package com.pending.model;

public enum JSONFile {
    ADMINS("json/admins.json"),
    BLACKLIST("json/blacklist.json"),
    CONTACTS("json/contacts.json"),
    HURRICANES("json/hurricanes.json"),
    REQUESTS("json/requests.json"),
    SHELTERS("json/shelters.json"),
    USERS("json/users.json"),
    VOLUNTEERS("json/volunteers.json");

    private String filename;

    JSONFile(String filename) {
        this.filename = filename;
    }
}
