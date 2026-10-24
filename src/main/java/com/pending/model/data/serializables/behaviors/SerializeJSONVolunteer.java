package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SerializeJSONVolunteer<T extends Volunteer> extends SerializeJSONUser<T> {
    public SerializeJSONVolunteer(T serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = super.serialize();
        jsonObject.put("id", serializable.getId());
        JSONArray reliefTypes = new JSONArray();
        for (ReliefType reliefType: serializable.getReliefTypes()) {
            reliefTypes.add(reliefType.name());
        }
        jsonObject.put("relief_types", reliefTypes);
        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);
        serializable.getReliefTypes().clear();
        for (Object reliefType: (JSONArray) jsonObject.get("relief_types")) {
            serializable.getReliefTypes().add(ReliefType.valueOf((String) reliefType));
        }
    }

    @Override
    public void resolveReferences() {
        super.resolveReferences();
    }
}