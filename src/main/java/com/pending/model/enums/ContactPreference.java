package com.pending.model.enums;

public enum ContactPreference {
    TEXT("TEXT"),
    CALL("CALL"),
    EMAIL("EMAIL");

    private String value;

    private ContactPreference(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}