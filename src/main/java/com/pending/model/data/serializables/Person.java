package com.pending.model.data.serializables;

import java.time.LocalDate;

public class Person extends LivingBeing {
    private String firstName;
    private String lastName;


    public Person(String firstName, String lastName, LocalDate dateOfBirth) {
        super(dateOfBirth, "human");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person() {
        this("", "", LocalDate.now());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
