package com.pending.model.data.serializables;

import java.util.UUID;

import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

public class SerializeJSONReliefRequest extends SerializeJSON<ReliefRequest> {
    public SerializeJSONReliefRequest(ReliefRequest serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId());
        jsonObject.put("status", serializable.getStatus().name());
        jsonObject.put("description", serializable.getDescription());
        jsonObject.put("location", serializable.getLocation().serialize());
        jsonObject.put("type", serializable.getType().name());

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setStatus(ReliefStatus.valueOf((String) jsonObject.get("status")));
        serializable.setDescription((String) jsonObject.get("description"));
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setType(ReliefType.valueOf((String) jsonObject.get("type")));
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(database.getLocationByUUID(serializable.getLocationUUIDReference()));
    }
}
