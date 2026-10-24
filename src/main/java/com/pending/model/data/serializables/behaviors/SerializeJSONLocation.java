package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONObject;

import com.pending.model.data.serializables.Location;

/**
 * Provides JSON serialization behavior for Location objects.
 * This class converts location fields to JSON and restores them from JSON.
 */
public class SerializeJSONLocation extends SerializeJSON<Location> {

    /**
     * Creates JSON serialization behavior for a location.
     *
     * @param serializable the location managed by this behavior
     */
    public SerializeJSONLocation(Location serializable) {
        super(serializable);
    }

    /**
     * Converts the location into a JSON object.
     *
     * @return the JSON representation of the location
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("state", serializable.getState());
        jsonObject.put("city", serializable.getCity());
        jsonObject.put("street", serializable.getStreet() == null
                ? ""
                : serializable.getStreet());
        jsonObject.put("zip", serializable.getZip() == null
                ? "00000"
                : serializable.getZip());

        return jsonObject;
    }

    /**
     * Loads location fields from a JSON object.
     *
     * @param jsonObject the JSON object containing location data
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setState((String) jsonObject.get("state"));
        serializable.setCity((String) jsonObject.get("city"));
        serializable.setStreet((String) jsonObject.get("street"));
        serializable.setZip((String) (jsonObject.get("zip") == null
                ? "00000"
                : jsonObject.get("zip")));
    }
}