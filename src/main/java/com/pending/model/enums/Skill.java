package com.pending.model.enums;

/**
 * Lists specialized skills a volunteer may use when providing relief aid.
 */
public enum Skill {
    /** Ability to provide medical-care assistance. */
    MEDICAL_CARE("Medical Care"),

    /** Ability to assist with evacuations. */
    EVACUATING("Evacuation"),

    /** Ability to distribute emergency resources. */
    RESOURCE_DISTRIBUTION("Resource Distribution"),

    /** Ability to provide transportation assistance. */
    TRANSPORTATION("Transportation");

    /** The readable display name for this skill. */
    private String name;

    /**
     * Creates a skill with its display name.
     *
     * @param name the display name for the skill
     */
    private Skill(String name) {
        this.name = name;
    }
}