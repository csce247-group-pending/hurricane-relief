package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Resource;
import com.pending.model.enums.ResourceType;
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
        serializable.setType(ResourceType.valueOf((String) jsonObject.get("type")));
        serializable.setQuantity(((Long) jsonObject.get("quantity")).intValue());
        serializable.setDescription((String) jsonObject.get("description"));
        serializable.setRestockDate(LocalDateTime.parse((String) jsonObject.get("restock_date")));
    }
}
