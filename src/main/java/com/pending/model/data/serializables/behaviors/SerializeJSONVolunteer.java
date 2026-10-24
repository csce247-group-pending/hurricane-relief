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
        JSONObject jsonObject = new JSONObject();
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
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.getReliefTypes().clear();
        for (Object reliefType: (JSONArray) jsonObject.get("relief_types")) {
            serializable.getReliefTypes().add(ReliefType.valueOf((String) reliefType));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        User user = database.getUserByUUID(serializable.getId());

        serializable.setFirstName(user.getFirstName());
        serializable.setLastName(user.getLastName());
        serializable.setDateOfBirth(user.getDateOfBirth());
        serializable.setType(user.getType());
        serializable.getReliefNeeds().clear();
        for (ReliefType reliefType : user.getReliefNeeds()) {
            serializable.addReliefNeed(reliefType);
        }

        serializable.setPassword(user.getPassword());

        serializable.setLocationUUIDReference(user.getLocationUUIDReference());
        serializable.setUserContactUUIDReference(user.getUserContactUUIDReference());
        serializable.setEmergencyContactUUIDReference(user.getEmergencyContactUUIDReference());

        serializable.getUserReliefRequestsUUIDReference().clear();
        serializable.getUserReliefRequestsUUIDReference().addAll(user.getUserReliefRequestsUUIDReference());

        super.resolveReferences();
    }
}