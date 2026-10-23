package com.pending.model.data.serializables.behaviors;

import com.pending.model.data.serializables.Serializable;

/**
 * Defines how a Serializable object is converted to and from stored data.
 *
 * @param <T> the Serializable object type handled by this behavior
 */
public abstract class SerializeBehavior<T extends Serializable> {
    protected T serializable;

    /**
     * Creates a serialization behavior for an object.
     *
     * @param serializable the object this behavior manages
     */
    public SerializeBehavior(T serializable) {
        this.serializable = serializable;
    }

    /**
     * Converts the object into a format that can be stored.
     *
     * @return the stored representation of the object
     */
    public abstract Object serialize();

    /**
     * Loads object data from a stored representation.
     *
     * @param object the stored object data
     */
    public abstract void deserialize(Object object);

    /**
     * Connects related objects after data has been loaded.
     * Most serialization behaviors do not need to override this method.
     */
    public void resolveReferences() {
        // Do nothing most of the time, overridden by User and
    }
}