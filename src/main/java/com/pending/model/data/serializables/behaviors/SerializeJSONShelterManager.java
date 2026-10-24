package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;
import com.pending.model.data.serializables.Volunteer;
import org.json.simple.JSONObject;

import java.util.UUID;

public class SerializeJSONShelterManager<T extends ShelterManager> extends SerializeJSONVolunteer<T> {
    public SerializeJSONShelterManager(T serializable) {
        super(serializable);
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        serializable.setShelterUUIDReference(UUID.fromString((String) jsonObject.get("shelter")));
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = super.serialize();
        jsonObject.put("shelter", serializable.getShelter().getId());
        return jsonObject;
    }

    @Override
    public void resolveReferences() {
        super.resolveReferences();
        Database database = Database.getInstance();
        serializable.setShelter(database.getShelterByUUID(serializable.getShelterUUIDReference()));
    }
}
