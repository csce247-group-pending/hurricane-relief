package com.pending.model.data.serializables;

import org.json.simple.JSONObject;

public class SerializeJSONLocation extends SerializeJSON<Location> {
    public SerializeJSONLocation(Location serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", serializable.getState());
        jsonObject.put("city", serializable.getCity());
        jsonObject.put("county", serializable.getCounty());
        jsonObject.put("street", serializable.getStreet());
        jsonObject.put("zip", serializable.getZip());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setState((String) jsonObject.get("state"));
        serializable.setCity((String) jsonObject.get("city"));
        serializable.setCounty((String) jsonObject.get("county"));
        serializable.setStreet((String) jsonObject.get("street"));
        serializable.setZip((String) jsonObject.get("zip"));
    }
}
