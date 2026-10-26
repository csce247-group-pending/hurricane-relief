package com.pending.model.enums;

/**
 * Represents the current availability status of a shelter.
 */
public enum ShelterStatus {
    /** The shelter is open and may accept new occupants. */
    OPEN,

    /** The shelter is permanently or currently closed. */
    CLOSED,

    /** The shelter has reached its maximum capacity. */
    FULL,

    /** The shelter is temporarily unable to accept occupants. */
    TEMP_UNAVAILABLE
}