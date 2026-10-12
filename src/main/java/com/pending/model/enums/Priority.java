package com.pending.model.enums;

public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private int value;

    private Priority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
