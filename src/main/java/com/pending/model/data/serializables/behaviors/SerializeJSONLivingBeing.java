package com.pending.model.data.serializables.behaviors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.pending.model.data.serializables.LivingBeing;
import com.pending.model.enums.LivingBeingType;
import com.pending.model.enums.ReliefType;

/**
 * Provides JSON serialization behavior for LivingBeing objects.
 * This class stores and restores identity, date of birth, type,
 * and relief-need information.
 *
 * @param <T> the LivingBeing type handled by this behavior
 */
public class SerializeJSONLivingBeing<T extends LivingBeing>
        extends SerializeJSON<T> {

    /**
     * Creates JSON serialization behavior for a living being.
     *
     * @param serializable the living being managed by this behavior
     */
    public SerializeJSONLivingBeing(T serializable) {
        super(serializable);
    }

    /**
     * Converts the living being into a JSON object.
     *
     * @return the JSON representation of the living being
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", serializable.getId().toString());
        jsonObject.put("first_name", serializable.getFirstName());
        jsonObject.put("last_name", serializable.getLastName());
        jsonObject.put("date_of_birth",
                serializable.getDateOfBirth().toString());
        jsonObject.put("type", serializable.getType().name());

        JSONArray reliefNeeds = new JSONArray();

        for (ReliefType reliefType : serializable.getReliefNeeds()) {
            reliefNeeds.add(reliefType.name());
        }

        jsonObject.put("relief_needs", reliefNeeds);
        return jsonObject;
    }

    /**
     * Loads living-being fields from a JSON object.
     *
     * @param jsonObject the JSON object containing living-being data
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        serializable.setId(
                UUID.fromString((String) jsonObject.get("id")));
        serializable.setFirstName((String) jsonObject.get("first_name"));
        serializable.setLastName((String) jsonObject.get("last_name"));
        serializable.setDateOfBirth(
                LocalDate.parse((String) jsonObject.get("date_of_birth")));
        serializable.setType(
                LivingBeingType.valueOf((String) jsonObject.get("type")));

        ArrayList<ReliefType> reliefNeeds =
                serializable.getReliefNeeds();

        reliefNeeds.clear();

        JSONArray jsonReliefNeeds =
                (JSONArray) jsonObject.get("relief_needs");

        for (Object reliefType : jsonReliefNeeds) {
            reliefNeeds.add(
                    ReliefType.valueOf((String) reliefType));
        }
    }
}