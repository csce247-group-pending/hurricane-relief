package com.pending.model.data.serializables;

import java.time.LocalDate;
import java.util.ArrayList;

import com.pending.model.enums.ReliefType;

public class Person extends LivingBeing {
    private String lastName;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, ArrayList<ReliefType> reliefNeeds) {
        super(firstName, dateOfBirth, "human", reliefNeeds);
        this.lastName = lastName;
    }

    public Person() {
        this("", "", LocalDate.now(), new ArrayList<>());
    }

    public String getFirstName() {
        return getPrimaryName();
    }

    public void setFirstName(String firstName) {
        setPrimaryName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
