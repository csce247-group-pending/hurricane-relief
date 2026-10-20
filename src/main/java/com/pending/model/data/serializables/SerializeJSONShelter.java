package com.pending.model.data.serializables;

import com.pending.model.data.DataList;
import com.pending.model.data.Database;
import com.pending.model.enums.ShelterStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

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

        JSONArray resources = new JSONArray();
        for (Resource resource: serializable.getResources()) {
            resources.add(resource.serialize());
        }
        jsonObject.put("resources", resources);

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

        serializable.getResources().clear();
        JSONArray resources = (JSONArray) jsonObject.get("resources");
        for (Object jsonResource: resources) {
            Resource resource = new Resource();
            resource.deserialize(jsonResource);
            serializable.getResources().add(resource);
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
