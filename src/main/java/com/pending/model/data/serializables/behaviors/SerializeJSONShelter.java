package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ShelterStatus;

public class SerializeJSONShelter extends SerializeJSON<Shelter> {
    public SerializeJSONShelter(Shelter serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("name", serializable.getName());
        jsonObject.put("location", serializable.getLocation().serialize());
        jsonObject.put("capacity", serializable.getCapacity());
        jsonObject.put("occupancy", serializable.getOccupants());

        JSONArray volunteers = new JSONArray();
        for (Volunteer volunteer: serializable.getVolunteers()) {
            volunteers.add(volunteer.getId().toString());
        }
        jsonObject.put("volunteers", volunteers);

        jsonObject.put("status", serializable.getStatus().name());
        jsonObject.put("contact", serializable.getContactInfo().serialize());

        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) (jsonObject.get("id") == null? "" : jsonObject.get("id"))));
        serializable.setName((String) jsonObject.get("name"));
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setCapacity(((Long) jsonObject.get("capacity")).intValue());
        serializable.setContactUUIDReference(UUID.fromString((String) jsonObject.get("contact")));
//        serializable.setOccupants(((Long) jsonObject.get("occupancy")).intValue()); //TODO: fix this


        JSONArray volunteersByUUID = (JSONArray) jsonObject.get("volunteers");
        serializable.getVolunteersUUIDReference().clear();
        for (Object jsonId: volunteersByUUID) {
            UUID id = UUID.fromString((String) jsonId);
            serializable.getVolunteersUUIDReference().add(id);
        }

        serializable.setStatus(ShelterStatus.valueOf((String) jsonObject.get("status")));
        serializable.setManagerUUIDReference(jsonObject.get("manager") == null? null : UUID.fromString((String) jsonObject.get("manger")));
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(database.getLocationByUUID(serializable.getLocationUUIDReference()));
        serializable.setContactInfo(database.getContactByUUID(serializable.getContactUUIDReference()));

        serializable.getVolunteersUUIDReference().clear();
        for (UUID id: serializable.getVolunteersUUIDReference()) {
            Volunteer volunteer = (Volunteer) database.getUserByUUID(id);
            serializable.getVolunteers().add(volunteer);
        }

        if (serializable.getManagerUUIDReference() != null) {
            serializable.setManager((ShelterManager) database.getUserByUUID(serializable.getManagerUUIDReference()));
        }
    }
}
