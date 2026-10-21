package com.pending.model.enums;

public enum Skill {
    MEDICAL_CARE("Medical Care"),
    EVACUATING("Evacuation"),
    RESOURCE_DISTRIBUTION("Resource Distribution"),
    TRANSPORTATION("Transportation");

    private String name;

    private Skill(String name) {
        this.name = name;
    }
}
