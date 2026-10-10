package com.pending.model;

import com.pending.model.data.serializables.*;

import java.util.ArrayList;
import java.util.UUID;

public class Admin extends User {
    public Admin(UUID id, String firstName, String lastName, String password,
                 Location location, Contact userContact, Contact emergencyContact) {
        super(id, firstName, lastName, password,
            location, emergencyContact, emergencyContact);
    }

    public void deactivateUser(User user) {

    }

    public void assignVolunteer(ReliefRequest request, Volunteer volunteer) {

    }
    
    public void manageResource(Resource resource) {

    }

    public void manageShelter(Shelter shelter) {

    }

    public ArrayList<ReliefRequest> viewAllReliefRequests() {
        return null;
    }
}