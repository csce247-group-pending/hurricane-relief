package com.pending.model.enums;

/**
 * Lists the types of assistance that may be requested or provided.
 */
public enum ReliefType {
    /** Food assistance. */
    FOOD("Food"),

    /** Water assistance. */
    WATER("Water"),

    /** Medical-care assistance. */
    MEDICAL_CARE("Medical Care"),

    /** Shelter assistance. */
    SHELTER("Shelter"),

    /** Shelter assistance for pets. */
    PET_SHELTER("Pet Shelter"),

    /** Evacuation assistance. */
    EVACUATION("Evacuation"),

    /** Transportation assistance. */
    TRANSPORTATION("Transportation"),

    /** A type of assistance not otherwise listed. */
    OTHER("Other");

    /** The readable name displayed for this relief type. */
    private String name;

    /**
     * Creates a relief type with its readable display name.
     *
     * @param name the display name
     */
    private ReliefType(String name) {
        this.name = name;
    }

    /**
     * Returns this relief type's readable display name.
     *
     * @return the display name
     */
    @Override
    public String toString() {
        return name;
    }
}