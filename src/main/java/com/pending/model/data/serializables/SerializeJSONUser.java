package com.pending.model.data.serializables;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class SerializeJSONUser extends SerializeJSON<User> {
    public SerializeJSONUser(User serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("first_name", serializable.getFirstName());
        jsonObject.put("last_name", serializable.getLastName());
        jsonObject.put("password", serializable.getPassword());
        jsonObject.put("location", serializable.getLocation().serialize());
        jsonObject.put("user_contact", serializable.getUserContact().getId().toString());
        jsonObject.put("emergency_contact", serializable.getUserContact().getId().toString());

        JSONArray userReliefRequests = new JSONArray();
        for (UUID id: serializable.getUserReliefRequestsUUIDReference()) {
        for (UUID id: serializable.getUserReliefRequestsUUIDReference()) {
            userReliefRequests.add(id.toString());
        }

        jsonObject.put("user_relief_requests", userReliefRequests);

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setFirstName((String) jsonObject.get("firstName"));
        serializable.setLastName((String) jsonObject.get("lastName"));
        serializable.setPassword((String) jsonObject.get("password"));

        String locationJsonObject = (String) jsonObject.get("location");
        serializable.getLocation().deserialize(locationJsonObject);

        serializable.setUserContactUUIDReference(UUID.fromString((String) jsonObject.get("user_contact")));
        serializable.setEmergencyContactUUIDReference(UUID.fromString((String) jsonObject.get("emergency_contact")));

        ArrayList<UUID> userReliefRequestsByUUID = serializable.getUserReliefRequestsUUIDReference();
        ArrayList<UUID> userReliefRequestsByUUID = serializable.getUserReliefRequestsUUIDReference();
        for (Object jsonReliefRequestUUID: userReliefRequestsByUUID) {
            userReliefRequestsByUUID.add(UUID.fromString((String) jsonReliefRequestUUID));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();
        serializable.setUserContact(database.getContactByUUID(serializable.getUserContactUUIDReference()));
        serializable.setEmergencyContact(database.getContactByUUID(serializable.getEmergencyContactUUIDReference()));

        ArrayList<ReliefRequest> userReliefRequests = serializable.getUserReliefRequests();
        for (UUID id: serializable.getUserReliefRequestsUUIDReference()) {
        for (UUID id: serializable.getUserReliefRequestsUUIDReference()) {
            userReliefRequests.add(database.getReliefRequestByUUID(id));
        }
    }
}
