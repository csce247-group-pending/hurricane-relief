package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Hurricane;
import com.pending.model.data.serializables.Location;
import com.pending.model.enums.HurricaneStatus;

/**
 * Provides JSON serialization behavior for Hurricane objects.
 * Hurricane locations are stored as UUID references and resolved
 * after all data files have loaded.
 */
public class SerializeJSONHurricane extends SerializeJSON<Hurricane> {

    /**
     * Creates JSON serialization behavior for a hurricane.
     *
     * @param serializable the hurricane managed by this behavior
     */
    public SerializeJSONHurricane(Hurricane serializable) {
        super(serializable);
    }

    /**
     * Converts a hurricane into a JSON object.
     *
     * @return the JSON representation of the hurricane
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("name", serializable.getName());
        jsonObject.put("category", serializable.getCategory());

        JSONArray locations = new JSONArray();

        if (serializable.getLocations().isEmpty()) {
            for (UUID id : serializable.getLocationsUUIDReference()) {
                locations.add(id.toString());
            }
        } else {
            for (Location location : serializable.getLocations()) {
                if (location != null) {
                    locations.add(location.getId().toString());
                }
            }
        }

        jsonObject.put("locations", locations);
        jsonObject.put("status", serializable.getStatus().name());

        return jsonObject;
    }

    /**
     * Loads hurricane fields and location UUID references from JSON.
     *
     * @param jsonObject the JSON object containing hurricane data
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setName((String) jsonObject.get("name"));
        serializable.setCategory(
                ((Long) jsonObject.get("category")).intValue());

        ArrayList<UUID> locations =
                serializable.getLocationsUUIDReference();

        locations.clear();

        for (Object locationUUIDJson :
                (JSONArray) jsonObject.get("locations")) {

            locations.add(
                    UUID.fromString((String) locationUUIDJson));
        }

        serializable.setStatus(
                HurricaneStatus.valueOf(
                        (String) jsonObject.get("status")));
    }

    /**
     * Replaces loaded location UUID references with Location objects.
     */
    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.getLocations().clear();

        for (UUID id : serializable.getLocationsUUIDReference()) {
            Location location = database.getLocationByUUID(id);

            if (location != null) {
                serializable.getLocations().add(location);
            }
        }
    }
}