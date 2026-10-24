package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import com.pending.model.data.serializables.behaviors.SerializeJSONLivingBeing;
import com.pending.model.enums.JSONFile;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

/**
 * Represents a person or pet receiving assistance during a disaster.
 * A living being has identifying information, a date of birth, a type,
 * and a list of relief needs.
 */
public class LivingBeing extends Serializable {
    /** The JSON file used to store living-being data. */
    public static final String file = JSONFile.LIVING_BEINGS.getFilename();

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LivingBeingType type;
    private ArrayList<ReliefType> reliefNeeds;

    /**
     * Creates a living being with identifying information and relief needs.
     *
     * @param firstName the living being's first name
     * @param lastName the living being's last name
     * @param dateOfBirth the living being's date of birth
     * @param type whether the living being is a human or pet
     * @param reliefNeeds the assistance types the living being needs
     */
    public LivingBeing(String firstName, String lastName,
            LocalDate dateOfBirth, LivingBeingType type,
            ArrayList<ReliefType> reliefNeeds) {

        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
        setReliefNeeds(reliefNeeds);

        serializeBehavior = new SerializeJSONLivingBeing<>(this);
    }

    /**
     * Creates a default pet living being with empty identifying information.
     */
    public LivingBeing() {
        this("", "", LocalDate.now(), LivingBeingType.PET,
                new ArrayList<>());
    }

    /**
     * Gets the living being's date of birth.
     *
     * @return the date of birth
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the living being's date of birth.
     *
     * @param dateOfBirth the new date of birth
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the type of living being.
     *
     * @return the living being type
     */
    public LivingBeingType getType() {
        return type;
    }

    /**
     * Sets the type of living being.
     *
     * @param type the new living being type
     */
    public void setType(LivingBeingType type) {
        this.type = type;
    }

    /**
     * Gets the first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the list of needed relief types.
     *
     * @return the list of relief needs
     */
    public ArrayList<ReliefType> getReliefNeeds() {
        return reliefNeeds;
    }

    /**
     * Sets the list of needed relief types.
     *
     * @param reliefNeeds the new relief-needs list; an empty list is used
     *                    when the provided list is null
     */
    public void setReliefNeeds(ArrayList<ReliefType> reliefNeeds) {
        this.reliefNeeds = reliefNeeds == null
                ? new ArrayList<>()
                : reliefNeeds;
    }

    /**
     * Adds a relief need.
     *
     * @param need the relief type to add
     * @return true if the need was added
     */
    public boolean addReliefNeed(ReliefType need) {
        return reliefNeeds.add(need);
    }

    /**
     * Removes a relief need.
     *
     * @param need the relief type to remove
     * @return true if the need was removed
     */
    public boolean removeReliefNeed(ReliefType need) {
        return reliefNeeds.remove(need);
    }

    /**
     * Calculates the living being's current age in years.
     *
     * @return the age in years
     */
    public int getAge() {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Returns a readable summary of the living being.
     *
     * @return a summary containing the name, type, and age
     */
    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName
                + " is a " + type + " Age: " + getAge();
    }
}