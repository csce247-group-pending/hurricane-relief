package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;

public class SerializeJSONUser<T extends User> extends SerializeJSONLivingBeing<T> {
    public SerializeJSONUser(T serializable) {
        super(serializable);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        UUID locationId = serializable.getLocation() == null
                ? serializable.getLocationUUIDReference()
                : serializable.getLocation().getId();

        UUID userContactId = serializable.getUserContact() == null
                ? serializable.getUserContactUUIDReference()
                : serializable.getUserContact().getId();

        UUID emergencyContactId = serializable.getEmergencyContact() == null
                ? serializable.getEmergencyContactUUIDReference()
                : serializable.getEmergencyContact().getId();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("first_name", serializable.getFirstName());
        jsonObject.put("last_name", serializable.getLastName());
        jsonObject.put("password", serializable.getPassword());
        jsonObject.put("location", locationId.toString());
        jsonObject.put("user_contact", userContactId.toString());
        jsonObject.put("emergency_contact", emergencyContactId.toString());

        JSONArray userReliefRequests = new JSONArray();
        for (UUID id : serializable.getUserReliefRequestsUUIDReference()) {
            userReliefRequests.add(id.toString());
        }

        jsonObject.put("user_relief_requests", userReliefRequests);

        return jsonObject;
    }

    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(UUID.fromString((String) jsonObject.get("id")));
        serializable.setFirstName((String) jsonObject.get("first_name"));
        serializable.setLastName((String) jsonObject.get("last_name"));
        serializable.setPassword((String) jsonObject.get("password"));

        serializable.setLocationUUIDReference(
                UUID.fromString((String) jsonObject.get("location")));

        serializable.setUserContactUUIDReference(
                UUID.fromString((String) jsonObject.get("user_contact")));

        serializable.setEmergencyContactUUIDReference(
                UUID.fromString((String) jsonObject.get("emergency_contact")));

        ArrayList<UUID> userReliefRequestsByUUID =
                serializable.getUserReliefRequestsUUIDReference();

        userReliefRequestsByUUID.clear();

        for (Object jsonReliefRequestUUID :
                (JSONArray) jsonObject.get("user_relief_requests")) {
            userReliefRequestsByUUID.add(
                    UUID.fromString((String) jsonReliefRequestUUID));
        }
    }

    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setUserContact(
                database.getContactByUUID(serializable.getUserContactUUIDReference()));

        serializable.setEmergencyContact(
                database.getContactByUUID(serializable.getEmergencyContactUUIDReference()));

        serializable.setLocation(
                database.getLocationByUUID(serializable.getLocationUUIDReference()));

        ArrayList<ReliefRequest> userReliefRequests =
                serializable.getUserReliefRequests();

        userReliefRequests.clear();

        for (UUID id : serializable.getUserReliefRequestsUUIDReference()) {
            userReliefRequests.add(database.getReliefRequestByUUID(id));
        }
    }
}