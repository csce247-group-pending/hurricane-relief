package com.pending.model.data.serializables;

import org.json.simple.JSONObject;

import com.pending.model.enums.ResourceType;

public class SerializeJSONResource extends SerializeJSON<Resource> {
    public SerializeJSONResource(Resource serializable) {
        super(serializable);
    }

        @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", serializable.getType().name());
        jsonObject.put("quantity", serializable.getQuantity());
        jsonObject.put("description", serializable.getDescription());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setType(ResourceType.valueOf((String) jsonObject.get("type")));
        serializable.setQuantity(((Long) jsonObject.get("quantity")).intValue());
        serializable.setDescription((String) jsonObject.get("description"));
    }
}
