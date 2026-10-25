package com.pending.model.enums;

public enum ReliefType {
    FOOD("Food"),
    WATER("Water"),
    MEDICAL_CARE("Medical Care"),
    SHELTER("Shelter"),
    PET_SHELTER("Pet Shelter"),
    EVACUATION("Evacuation"),
    TRANSPORTATION("Transportation"),
    OTHER("Other");

    private String name;

    private ReliefType(String name) {
        this.name = name;
    }

    @Override 
    public String toString() {
        return name;
    }
}
