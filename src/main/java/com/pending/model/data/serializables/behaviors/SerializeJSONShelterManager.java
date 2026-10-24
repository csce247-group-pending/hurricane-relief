package com.pending.model.data.serializables.behaviors;

import java.util.UUID;

import org.json.simple.JSONObject;

import com.pending.model.data.Database;
import com.pending.model.data.serializables.Shelter;
import com.pending.model.data.serializables.ShelterManager;

/**
 * Provides JSON serialization behavior for shelter manager objects.
 *
 * @param <T> the shelter manager type handled by this serializer
 */
public class SerializeJSONShelterManager<T extends ShelterManager>
        extends SerializeJSONVolunteer<T> {

    /**
     * Creates JSON serialization behavior for a shelter manager.
     *
     * @param serializable the shelter manager being serialized
     */
    public SerializeJSONShelterManager(T serializable) {
        super(serializable);
    }

    /**
     * Loads shelter manager data from JSON, including the UUID of the
     * shelter managed by this user.
     *
     * @param jsonObject the JSON data for the shelter manager
     */
    @Override
    public void deserialize(JSONObject jsonObject) {
        super.deserialize(jsonObject);

        Object shelterId = jsonObject.get("shelter");

        serializable.setShelterUUIDReference(
                shelterId == null ? null : UUID.fromString((String) shelterId));
    }

    /**
     * Converts the shelter manager into JSON. Volunteer fields are handled
     * by the parent serializer, while this method adds the managed shelter ID.
     *
     * @return the JSON representation of the shelter manager
     */
    @Override
    public JSONObject serialize() {
        JSONObject jsonObject = super.serialize();

        UUID shelterId = serializable.getShelter() == null
                ? serializable.getShelterUUIDReference()
                : serializable.getShelter().getId();

        jsonObject.put("shelter",
                shelterId == null ? null : shelterId.toString());

        return jsonObject;
    }

    /**
     * Reconnects the shelter manager to its managed shelter after all JSON
     * data has been loaded.
     */
    @Override
    public void resolveReferences() {
        super.resolveReferences();

        Database database = Database.getInstance();

        serializable.setShelter(
                database.getShelterByUUID(
                        serializable.getShelterUUIDReference()));
    }
}