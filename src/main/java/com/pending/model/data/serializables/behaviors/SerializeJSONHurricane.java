package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.enums.HurricaneStatus;

public class SerializeJSONHurricane extends SerializeJSON<Hurricane> {
    public SerializeJSONHurricane(Hurricane serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId());
        jsonObject.put("name", serializable.getName());
        jsonObject.put("category", serializable.getCategory());
        JSONArray locations = new JSONArray();
        for (Location location: serializable.getLocations()) {
            locations.add(location.getId().toString());
        }
        jsonObject.put("locations", locations);
        jsonObject.put("status", serializable.getStatus().name());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setName((String) jsonObject.get("name"));
        serializable.setCategory(((Long) jsonObject.get("category")).intValue());
        serializable.getLocationsUUIDReference().clear();
        ArrayList<UUID> locations = serializable.getLocationsUUIDReference();
        locations.clear();
        for (Object locationUUIDJson: (JSONArray) jsonObject.get("locations")) {
            locations.add(UUID.fromString((String) locationUUIDJson));
        }
        serializable.setStatus(HurricaneStatus.valueOf((String) jsonObject.get("status")));
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.getLocations().clear();
        for (UUID id: serializable.getLocationsUUIDReference()) {
            serializable.getLocations().add(database.getLocationByUUID(id));
        }

    }
}
