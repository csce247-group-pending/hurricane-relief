package com.pending.model.data.serializables;

import java.util.UUID;

import com.pending.model.data.serializables.behaviors.SerializeBehavior;

/**
 * Base class for objects that can be stored and restored from application
 * data files. Each serializable object has a UUID and a serialization
 * behavior that controls its conversion to and from stored data.
 */
public abstract class Serializable {
    /** The unique identifier for this object. */
    protected UUID id;

    /** The behavior used to serialize and deserialize this object. */
    protected SerializeBehavior<? extends Serializable> serializeBehavior;

    /** The data-file path associated with a serializable class. */
    public static String file;

    /**
     * Creates a serializable object with a newly generated UUID.
     */
    public Serializable() {
        id = UUID.randomUUID();
    }

    /**
     * Gets this object's UUID.
     *
     * @return the unique object identifier
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets this object's UUID.
     *
     * @param id the UUID to assign
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Converts this object into its stored-data representation.
     *
     * @return the serialized representation of this object
     */
    public Object serialize() {
        return serializeBehavior.serialize();
    }

    /**
     * Loads this object's fields from stored data.
     *
     * @param object the stored representation of this object
     */
    public void deserialize(Object object) {
        if (serializeBehavior == null) {
            System.out.println(this.getClass().getName()
                    + " has no serialize behavior!");
        } else {
            serializeBehavior.deserialize(object);
        }
    }

    /**
     * Connects UUID references to their related loaded objects.
     */
    public void resolveReferences() {
        serializeBehavior.resolveReferences();
    }
}