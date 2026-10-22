package com.pending.model.data.serializables;

import com.pending.model.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Admin extends User implements Observer {
    public Admin(UUID id, String firstName, String lastName, LocalDate dateOfBirth, String password,
                 Location location, Contact userContact, Contact emergencyContact) {
        super(id, firstName, lastName, dateOfBirth, password,
            location, emergencyContact, emergencyContact);
    }
}