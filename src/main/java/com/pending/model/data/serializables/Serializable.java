package com.pending.model.data.serializables;

import org.json.simple.JSONObject;

import java.util.UUID;

public abstract class Serializable {
    protected UUID id;
    protected SerializeBehavior<? extends Serializable> serializeBehavior;
    public static String file;

    public Serializable() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Object serialize() {
        return serializeBehavior.serialize();
    }

    public void deserialize(Object object) {
        serializeBehavior.deserialize(object);
    }
}
