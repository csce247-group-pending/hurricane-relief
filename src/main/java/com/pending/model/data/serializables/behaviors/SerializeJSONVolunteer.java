package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Volunteer;
import com.pending.model.enums.ReliefType;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
     * serializer, while this method adds the volunteer's relief skills.
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

        return jsonObject;
    }

    /**
     * Loads volunteer-specific data from JSON, including relief skills.
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
                serializable.getReliefTypes().add(
                        ReliefType.valueOf((String) reliefType));
            }
        }
    }

    /**
     * Resolves the volunteer's inherited user references after loading.
     */
    @Override
    public void resolveReferences() {
        super.resolveReferences();
    }
}