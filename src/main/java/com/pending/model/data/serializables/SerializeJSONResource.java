package com.pending.model.data.serializables;

import com.pending.model.ResourceType;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

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
        jsonObject.put("restock_date", serializable.getRestockDate().toString());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setType(ResourceType.values()[(int) jsonObject.get("type")]);
        serializable.setQuantity((int) jsonObject.get("quantity"));
        serializable.setDescription((String) jsonObject.get("description"));
        serializable.setRestockDate(LocalDateTime.parse((String) jsonObject.get("restock_date")));
    }
}
