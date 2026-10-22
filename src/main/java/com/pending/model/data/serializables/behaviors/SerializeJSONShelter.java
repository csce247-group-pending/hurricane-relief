package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Resource;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ShelterStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
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
        jsonObject.put("occupancy", serializable.getOccupancy());

        JSONArray volunteers = new JSONArray();
        for (Volunteer volunteer: serializable.getVolunteers()) {
            volunteers.add(volunteer.getId().toString());
        }
        jsonObject.put("volunteers", volunteers);

        jsonObject.put("status", serializable.getStatus().name());

        return jsonObject;
    }

    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setName((String) jsonObject.get("name"));
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setCapacity(((Long) jsonObject.get("capacity")).intValue());
        serializable.setOccupancy(((Long) jsonObject.get("occupancy")).intValue());

        JSONArray volunteersByUUID = (JSONArray) jsonObject.get("volunteers");
        serializable.getVolunteersUUIDReferecne().clear();
        for (Object jsonId: volunteersByUUID) {
            UUID id = UUID.fromString((String) jsonId);
            serializable.getVolunteersUUIDReferecne().add(id);
        }

        serializable.setStatus(ShelterStatus.valueOf((String) jsonObject.get("status")));
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(database.getLocationByUUID(serializable.getLocationUUIDReference()));

        serializable.getVolunteersUUIDReferecne().clear();
        for (UUID id: serializable.getVolunteersUUIDReferecne()) {
            Volunteer volunteer = (Volunteer) database.getUserByUUID(id);
            serializable.getVolunteers().add(volunteer);
        }
    }
}
