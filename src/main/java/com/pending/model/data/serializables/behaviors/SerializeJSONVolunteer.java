package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;

/**
 * Provides JSON serialization behavior for volunteer objects.
 *
 * @param <T> the volunteer type handled by this serializer
 */
public class SerializeJSONVolunteer<T extends Volunteer>
        extends SerializeJSONUser<T> {

    /**
     * Creates JSON serialization behavior for a volunteer.
     *
     * @param serializable the volunteer being serialized
     */
    public SerializeJSONVolunteer(T serializable) {
        super(serializable);
    }

    /**
     * Converts a volunteer into JSON. User fields are handled by the parent
     * serializer, while this method adds relief skills and the assigned-task
     * UUID reference.
     *
     * @return the JSON representation of the volunteer
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = super.serialize();

        jsonObject.put("id", serializable.getId().toString());

        JSONArray reliefTypes = new JSONArray();

        for (ReliefType reliefType : serializable.getReliefTypes()) {
            reliefTypes.add(reliefType.name());
        }

        jsonObject.put("relief_types", reliefTypes);

        if (serializable.getTask() != null) {
            jsonObject.put("task",
                    serializable.getTaskUUIDReference().toString());
        } else {
            jsonObject.put("task", null);
        }

        return jsonObject;
    }

    /**
     * Loads volunteer-specific data from JSON, including relief skills and
     * the UUID reference for an assigned task.
     *
     * @param jsonObject the JSON data for the volunteer
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);

        serializable.getReliefTypes().clear();

        JSONArray reliefTypes =
                (JSONArray) jsonObject.get("relief_types");

        if (reliefTypes != null) {
            for (Object reliefType : reliefTypes) {
                serializable.addReliefType(
                        ReliefType.valueOf((String) reliefType));
            }
        }

        if (jsonObject.get("task") != null) {
            serializable.setTaskUUIDReference(
                    UUID.fromString((String) jsonObject.get("task")));
        }
    }

    /**
     * Resolves inherited user references and restores the volunteer's
     * assigned relief request from its stored UUID reference.
     */
    @Override
    public void resolveReferences() {
        super.resolveReferences();

        serializable.setTask(
                Database.getInstance().getReliefRequestByUUID(
                        serializable.getTaskUUIDReference()));
    }
}