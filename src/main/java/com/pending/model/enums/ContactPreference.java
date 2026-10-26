package com.pending.model.enums;

/**
 * Defines the available ways a contact prefers to receive communication.
 */
public enum ContactPreference {
    /** Communication by text message. */
    TEXT("TEXT"),

    /** Communication by phone call. */
    CALL("CALL"),

    /** Communication by email. */
    EMAIL("EMAIL");

    /** The stored text value for this preference. */
    private String value;

    /**
     * Creates a contact preference with its stored text value.
     *
     * @param value the text value for the preference
     */
    private ContactPreference(String value) {
        this.value = value;
    }

    /**
     * Gets the stored text value for this preference.
     *
     * @return the text value
     */
    public String getValue() {
        return value;
    }
}