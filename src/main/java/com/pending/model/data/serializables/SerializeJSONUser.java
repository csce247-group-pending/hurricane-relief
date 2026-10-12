package com.pending.model.data.serializables;

import com.pending.model.data.Database;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

public class SerializeJSONUser extends SerializeJSON<User> {
    public SerializeJSONUser(User serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("firstName", serializable.getFirstName());
        jsonObject.put("lastName", serializable.getLastName());
        jsonObject.put("password", serializable.getPassword());
        jsonObject.put("location", serializable.getLocation().serialize());
        jsonObject.put("userContact", serializable.getUserContact().getId().toString());
        jsonObject.put("emergencyContact", serializable.getUserContact().getId().toString());

        JSONArray userReliefRequests = new JSONArray();
        for (UUID id: serializable.getUserReliefRequests()) {
            userReliefRequests.add(id.toString());
        }

        jsonObject.put("userReliefRequests", userReliefRequests);

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setFirstName((String) jsonObject.get("firstName"));
        serializable.setLastName((String) jsonObject.get("lastName"));
        serializable.setPassword((String) jsonObject.get("password"));

        JSONObject locationJsonObject = (JSONObject) jsonObject.get("location");
        serializable.getLocation().deserialize(locationJsonObject);
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();
        serializable.setUserContact(database.getContactByUUID(serializable.getUserContactUUIDReference()));
        serializable.setEmergencyContact(database.getContactByUUID(serializable.getEmergencyContactUUIDReference()));
    }
}
