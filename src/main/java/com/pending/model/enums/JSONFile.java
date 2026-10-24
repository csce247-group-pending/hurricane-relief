package com.pending.model.enums;

public enum JSONFile {
    ADMINS("json/admins.json"),
    BLACKLIST("json/blacklist.json"),
    CONTACTS("json/contacts.json"),
    HURRICANES("json/hurricanes.json"),
    REQUESTS("json/requests.json"),
    SHELTERS("json/shelters.json"),
    USERS("json/users.json"),
    VOLUNTEERS("json/volunteers.json"),
    LIVING_BEINGS("json/livingbeings.json"),
    SHELTER_MANAGERS("json/sheltermanagers.json");

    private String filename;

    JSONFile(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
