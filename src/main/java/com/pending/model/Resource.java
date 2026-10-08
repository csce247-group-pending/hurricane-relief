package com.pending.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serial;
import java.time.LocalDateTime;

public class Resource extends Serializable {
    private ResourceType type;
    private int quantity;
    private String description;
    private LocalDateTime restockDate;

    public Resource(ResourceType type, int quantity, String description) {

    }


    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type.ordinal());
        jsonObject.put("quantity", quantity);
        jsonObject.put("description", description);
        jsonObject.put("restock_date", restockDate.toString());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        type = ResourceType.values()[(int) jsonObject.get("type")];
        quantity = (int) jsonObject.get("quantity");
        description = (String) jsonObject.get("description");
        restockDate = LocalDateTime.parse((String) jsonObject.get("restock_date"));
    }
}
