package com.pending.model.enums;

/**
 * Lists the JSON data files used by the application.
 */
public enum JSONFile {

    /** Stores blacklisted-user records. */
    BLACKLIST("json/blacklist.json"),

    /** Stores contact records. */
    CONTACTS("json/contacts.json"),

    /** Stores hurricane records. */
    HURRICANES("json/hurricanes.json"),

    /** Stores relief-request records. */
    REQUESTS("json/requests.json"),

    /** Stores shelter records. */
    SHELTERS("json/shelters.json"),

    /** Stores standard user records. */
    USERS("json/users.json"),

    /** Stores volunteer records. */
    VOLUNTEERS("json/volunteers.json"),

    /** Stores living-being records. */
    LIVING_BEINGS("json/livingbeings.json"),

    /** Stores shelter-manager records. */
    SHELTER_MANAGERS("json/sheltermanagers.json");

    /** The relative path to the JSON file. */
    private String filename;

    /**
     * Creates a JSON file entry.
     *
     * @param filename the relative path to the JSON file
     */
    JSONFile(String filename) {
        this.filename = filename;
    }

    /**
     * Gets the relative path to this JSON file.
     *
     * @return the JSON file path
     */
    public String getFilename() {
        return filename;
    }
}