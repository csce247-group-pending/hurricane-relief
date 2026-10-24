package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ShelterStatus;
<<<<<<< HEAD
=======

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
>>>>>>> c070dad (Begin storage for living beings)

=======
>>>>>>> 3441401 (Refactor JSON data references and shelter occupants)
public class SerializeJSONShelter extends SerializeJSON<Shelter> {

    public SerializeJSONShelter(Shelter serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        UUID locationId = serializable.getLocation() == null
                ? serializable.getLocationUUIDReference()
                : serializable.getLocation().getId();

        UUID contactId = serializable.getContactInfo() == null
                ? serializable.getContactUUIDReference()
                : serializable.getContactInfo().getId();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("name", serializable.getName());
        jsonObject.put("location",
                locationId == null ? null : locationId.toString());
        jsonObject.put("contact",
                contactId == null ? null : contactId.toString());
        jsonObject.put("capacity", serializable.getCapacity());
        jsonObject.put("status", serializable.getStatus().name());
        jsonObject.put("accommodates_pets",
                serializable.accomodatesPets());

        JSONArray occupants = new JSONArray();

        if (serializable.getOccupants().isEmpty()) {
            for (UUID id : serializable.getOccupantsUUIDReference()) {
                occupants.add(id.toString());
            }
        } else {
            for (LivingBeing occupant : serializable.getOccupants()) {
                if (occupant != null) {
                    occupants.add(occupant.getId().toString());
                }
            }
        }

        jsonObject.put("occupants", occupants);

        JSONArray volunteers = new JSONArray();

        if (serializable.getVolunteers().isEmpty()) {
            for (UUID id : serializable.getVolunteersUUIDReference()) {
                volunteers.add(id.toString());
            }
        } else {
            for (Volunteer volunteer : serializable.getVolunteers()) {
                if (volunteer != null) {
                    volunteers.add(volunteer.getId().toString());
                }
            }
        }

        jsonObject.put("volunteers", volunteers);

        UUID managerId = serializable.getManager() == null
                ? serializable.getManagerUUIDReference()
                : serializable.getManager().getId();

        if (managerId != null) {
            jsonObject.put("manager", managerId.toString());
        }

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setName((String) jsonObject.get("name"));
<<<<<<< HEAD
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setCapacity(((Long) jsonObject.get("capacity")).intValue());
<<<<<<< HEAD
        serializable.setContactUUIDReference(UUID.fromString((String) jsonObject.get("contact")));
//        serializable.setOccupants(((Long) jsonObject.get("occupancy")).intValue()); //TODO: fix this

=======
=======
        serializable.setLocationUUIDReference(
                UUID.fromString((String) jsonObject.get("location")));
        serializable.setContactUUIDReference(
                UUID.fromString((String) jsonObject.get("contact")));
        serializable.setCapacity(
                ((Long) jsonObject.get("capacity")).intValue());
        serializable.setStatus(
                ShelterStatus.valueOf((String) jsonObject.get("status")));

        Object petsValue = jsonObject.get("accommodates_pets");
        serializable.setAccomodatesPets(
                petsValue != null && (Boolean) petsValue);

        serializable.getOccupantsUUIDReference().clear();
>>>>>>> 3441401 (Refactor JSON data references and shelter occupants)
        JSONArray occupants = (JSONArray) jsonObject.get("occupants");

        if (occupants != null) {
            for (Object occupantId : occupants) {
                serializable.getOccupantsUUIDReference().add(
                        UUID.fromString((String) occupantId));
            }
        }
>>>>>>> c070dad (Begin storage for living beings)

        serializable.getVolunteersUUIDReference().clear();
        JSONArray volunteers = (JSONArray) jsonObject.get("volunteers");

        if (volunteers != null) {
            for (Object volunteerId : volunteers) {
                serializable.getVolunteersUUIDReference().add(
                        UUID.fromString((String) volunteerId));
            }
        }

        Object managerId = jsonObject.get("manager");

        if (managerId == null) {
            serializable.setManagerUUIDReference(null);
        } else {
            serializable.setManagerUUIDReference(
                    UUID.fromString((String) managerId));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(
                database.getLocationByUUID(
                        serializable.getLocationUUIDReference()));

        serializable.setContactInfo(
                database.getContactByUUID(
                        serializable.getContactUUIDReference()));

        serializable.getOccupants().clear();

        for (UUID id : serializable.getOccupantsUUIDReference()) {
            LivingBeing occupant = database.getLivingBeingByUUID(id);

            if (occupant != null) {
                serializable.addOccupant(occupant);
            }
        }

        serializable.getVolunteers().clear();

        for (UUID id : serializable.getVolunteersUUIDReference()) {
            User user = database.getUserByUUID(id);

            if (user instanceof Volunteer) {
                serializable.addVolunteer((Volunteer) user);
            }
        }

        if (serializable.getManagerUUIDReference() != null) {
            User user = database.getUserByUUID(
                    serializable.getManagerUUIDReference());

            if (user instanceof ShelterManager) {
                serializable.setManager((ShelterManager) user);
            }
        }
    }
}