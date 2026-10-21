package com.pending.model.data.serializables;

import java.util.UUID;

public class Admin extends User {
    public Admin(UUID id, String firstName, String lastName, String password,
                 Location location, Contact userContact, Contact emergencyContact) {
        super(id, firstName, lastName, password,
            location, emergencyContact, emergencyContact);
    }
}