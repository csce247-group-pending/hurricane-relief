package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.ReliefRequest;
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

        JSONArray reliefTypes = new JSONArray();
        for (ReliefType type : serializable.getTypes()) {
            reliefTypes.add(type.name());
        }

        jsonObject.put("types", reliefTypes);
        jsonObject.put("contact", serializable.getContactInfo().serialize());
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setStatus(ReliefStatus.valueOf((String) jsonObject.get("status")));
        serializable.setDescription((String) jsonObject.get("description"));
        serializable.setLocationUUIDReference(UUID.fromString((String) jsonObject.get("location")));
        serializable.setContactUUIDReference(UUID.fromString((String) jsonObject.get("contact")));

        ArrayList<ReliefType> reliefTypes = serializable.getTypes();

        for (Object reliefType :
                (JSONArray) jsonObject.get("types")) {
            reliefTypes.add(ReliefType.valueOf((String) reliefType));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(database.getLocationByUUID(serializable.getLocationUUIDReference()));
        serializable.setContactInfo(database.getContactByUUID(serializable.getContactUUIDReference()));
    }
}
