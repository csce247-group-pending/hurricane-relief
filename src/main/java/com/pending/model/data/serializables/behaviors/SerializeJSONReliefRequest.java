package com.pending.model.data.serializables.behaviors;

import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.ReliefRequest;
import com.pending.model.data.serializables.User;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefStatus;
import com.pending.model.enums.ReliefType;

/**
 * Provides JSON serialization behavior for ReliefRequest objects.
 * Related locations, contacts, and assigned volunteers are stored
 * as UUID references.
 */
public class SerializeJSONReliefRequest
        extends SerializeJSON<ReliefRequest> {

    /**
     * Creates JSON serialization behavior for a relief request.
     *
     * @param serializable the relief request managed by this behavior
     */
    public SerializeJSONReliefRequest(ReliefRequest serializable) {
        super(serializable);
    }

    /**
     * Converts a relief request into a JSON object with UUID references.
     *
     * @return the JSON representation of the relief request
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        UUID locationId = serializable.getLocation() == null
                ? serializable.getLocationUUIDReference()
                : serializable.getLocation().getId();

        UUID contactId = serializable.getContactInfo() == null
                ? serializable.getContactUUIDReference()
                : serializable.getContactInfo().getId();

        UUID assignedToId = serializable.getAssignedTo() == null
                ? serializable.getAssignedToUUIDReference()
                : serializable.getAssignedTo().getId();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("status", serializable.getStatus().name());
        jsonObject.put("description", serializable.getDescription());
        jsonObject.put("location",
                locationId == null ? null : locationId.toString());
        jsonObject.put("contact",
                contactId == null ? null : contactId.toString());

        JSONArray reliefTypes = new JSONArray();

        for (ReliefType type : serializable.getTypes()) {
            reliefTypes.add(type.name());
        }

        jsonObject.put("types", reliefTypes);

        if (assignedToId != null) {
            jsonObject.put("assigned_to", assignedToId.toString());
        }

        return jsonObject;
    }

    /**
     * Loads relief-request fields and UUID references from a JSON object.
     *
     * @param jsonObject the JSON object containing relief-request data
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setStatus(
                ReliefStatus.valueOf((String) jsonObject.get("status")));
        serializable.setDescription(
                (String) jsonObject.get("description"));
        serializable.setLocationUUIDReference(
                UUID.fromString((String) jsonObject.get("location")));
        serializable.setContactUUIDReference(
                UUID.fromString((String) jsonObject.get("contact")));

        ArrayList<ReliefType> reliefTypes = serializable.getTypes();
        reliefTypes.clear();

        for (Object reliefType : (JSONArray) jsonObject.get("types")) {
            reliefTypes.add(
                    ReliefType.valueOf((String) reliefType));
        }

        Object assignedToId = jsonObject.get("assigned_to");

        serializable.setAssignedToUUIDReference(assignedToId == null
                ? null
                : UUID.fromString((String) assignedToId));
    }

    /**
     * Replaces loaded UUID references with their related objects.
     */
    @Override
    public void resolveReferences() {
        Database database = Database.getInstance();

        serializable.setLocation(
                database.getLocationByUUID(
                        serializable.getLocationUUIDReference()));

        serializable.setContactInfo(
                database.getContactByUUID(
                        serializable.getContactUUIDReference()));

        if (serializable.getAssignedToUUIDReference() != null) {
            User user = database.getUserByUUID(
                    serializable.getAssignedToUUIDReference());

            if (user instanceof Volunteer) {
                serializable.setAssignedTo((Volunteer) user);
            }
        }
    }
}