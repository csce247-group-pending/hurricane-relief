package com.pending.model.data.serializables;

import java.util.UUID;

import org.json.simple.JSONObject;

public class SerializeJSONLocation extends SerializeJSON<Location> {
    public SerializeJSONLocation(Location serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("state", serializable.getState());
        jsonObject.put("city", serializable.getCity());
        jsonObject.put("street", serializable.getStreet());
        jsonObject.put("zip", serializable.getZip());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setState((String) jsonObject.get("state"));
        serializable.setCity((String) jsonObject.get("city"));
        serializable.setStreet((String) jsonObject.get("street"));
        serializable.setZip((String) jsonObject.get("zip"));
    }
}
