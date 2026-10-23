package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Serializable;
import org.json.simple.JSONObject;

/**
 * Provides JSON serialization behavior for a Serializable object.
 *
 * @param <T> the Serializable object type handled by this behavior
 */
public abstract class SerializeJSON<T extends Serializable>
        extends SerializeBehavior<T> {

    /**
     * Creates JSON serialization behavior for an object.
     *
     * @param serializable the object this behavior manages
     */
    public SerializeJSON(T serializable) {
        super(serializable);
    }

    /**
     * Converts the object into a JSON object.
     *
     * @return the JSON representation of the object
     */
    public abstract JSONObject serialize();

    /**
     * Loads object data from a JSON object.
     *
     * @param jsonObject the JSON object containing stored data
     */
    public abstract void deserialize(JSONObject jsonObject);

    /**
     * Loads object data by converting a general object into a JSON object.
     *
     * @param object the stored JSON object data
     */
    public void deserialize(Object object) {
        deserialize((JSONObject) object);
    }
}